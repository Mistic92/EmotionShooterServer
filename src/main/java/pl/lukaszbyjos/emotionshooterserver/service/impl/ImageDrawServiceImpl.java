package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.ImageDrawService;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Slf4j
public class ImageDrawServiceImpl implements ImageDrawService {

    private BufferedImage foreground;
    private BufferedImage imageWithGradient;
    private BufferedImage background;

    private Color[] gColors = new Color[]{
            new Color(66, 133, 244, 100),
            new Color(219, 68, 55, 100),
            new Color(244, 180, 0, 100),
            new Color(15, 157, 88, 100)};

    private java.util.List<Color> colorList = Arrays.asList(gColors);
    /**
     * Max colors which can be in shuffled array. Can't be larger than gColors array
     */
    @Value("${effects.max-colors}")
    private int maxColorsInGradient;

    @Override
    public String createColorfullImage(VisionResponse visionResponse, Path filePath) {
        try {
            background = ImageIO.read(filePath.toFile());
            //prepare background
            foreground = new BufferedImage(background.getWidth(),
                    background.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            imageWithGradient = convertToARGB(background);

            Graphics2D foregroundGraphics = foreground.createGraphics();
            createForegraundColors(foregroundGraphics, foreground.getWidth(), foreground.getHeight());
            Graphics2D graphics = background.createGraphics();

            java.util.List<FaceAnnotation> faceAnnotationList = visionResponse.getFaceAnnotation();

            java.util.List<Point> points =
                    faceAnnotationList.stream()
                            .map(faceAnnotation -> faceAnnotation
                                    .getLandmarks()
                                    .stream()
                                    .filter(landmark ->
                                            landmark.getType().equals("MIDPOINT_BETWEEN_EYES")
                                                    || landmark.getType().equals("NOSE_TIP"))
                                    .map(Landmark::getPosition)
                                    .reduce((position, position2) -> {
                                        float x1 = position.getX();
                                        float y1 = position.getY();
                                        float x2 = position2.getX();
                                        float y2 = position2.getY();
                                        return new Position().setX((x1 + x2) / 2).setY((y1 + y2) / 2);
                                    })
                                    .get())
                            .map(position -> new Point((int) Math.ceil(position.getX()),
                                    (int) Math.ceil(position.getY())))
                            .collect(Collectors.toList());

//            for (FaceAnnotation faceAnnotation : faceAnnotationList) {
//                Position faceCenterPosition = faceAnnotation
//                        .getLandmarks()
//                        .stream()
//                        .filter(landmark ->
//                                landmark.getType().equals("MIDPOINT_BETWEEN_EYES")
//                                        || landmark.getType().equals("NOSE_TIP"))
//                        .map(Landmark::getPosition)
//                        .reduce((position, position2) -> {
//                            float x1 = position.getX();
//                            float y1 = position.getY();
//                            float x2 = position2.getX();
//                            float y2 = position2.getY();
//                            return new Position().setX((x1 + x2) / 2).setY((y1 + y2) / 2);
//                        })
//                        .get();
//                facePositions.add(faceCenterPosition);
//            }

            java.util.List<Integer> radiuses = faceAnnotationList.stream()
                    .map(FaceAnnotation::getBoundingPoly)
                    .map(BoundingPoly::getVertices)
                    .map(this::findRadius)
                    .collect(Collectors.toList());

            updateGradientAt(points, radiuses);

            //merge images
            graphics.drawImage(imageWithGradient, 0, 0, null);


            String newFilePath = filePath.toString().substring(0, filePath.toString().lastIndexOf(".jpg"));
            final File output = new File(newFilePath + "_fun.jpg");

            saveNewImage(background, output);

        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }


    /**
     * Shuffle color array and return new with size defined by maxColorsInGradient
     *
     * @return
     */
    private Color[] shuffleColorsRadnomColors() {
        Collections.shuffle(colorList);
        return colorList.stream().limit(maxColorsInGradient).toArray(Color[]::new);
    }

    private float[] getFractionsForColors() {
        switch (maxColorsInGradient) {
            case 1:
                return new float[]{0f};
            case 2:
                return new float[]{0.0f, 1f};
            case 3:
                return new float[]{0.0f, 0.5f, 1f};
            case 4:
                return new float[]{0.0f, 0.25f, 0.75f, 1.0f};
            default:
                return new float[]{1f};
        }
    }

    /**
     * Creates foreground gradient
     *
     * @param backGraphic
     * @param width
     * @param height
     */
    private void createForegraundColors(Graphics2D backGraphic, int width, int height) {
        float fractions[] = getFractionsForColors();
        LinearGradientPaint linearGradientPaint =
                new LinearGradientPaint(0,
                        0,
                        width,
                        height,
                        fractions,
                        shuffleColorsRadnomColors(),
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);

        backGraphic.setPaint(linearGradientPaint);
        backGraphic.fillRect(0, 0, foreground.getWidth(), foreground.getHeight());
    }


    /**
     * @param vertexList
     * @return
     */
    private int findRadius(java.util.List<Vertex> vertexList) {
        int[] xPos = new int[vertexList.size()];
        int[] yPos = new int[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v1 = vertexList.get(i);
            xPos[i] = v1.getX();
            yPos[i] = v1.getY();
        }
        int height = calculateHeight(xPos, yPos, vertexList.size());
        return (height + 150) / 2;
    }

    /*
    * Calculates the bounding box of the points passed to the constructor.
    * Sets <code>bounds</code> to the result.
    * @param xpoints[] array of <i>x</i> coordinates
    * @param ypoints[] array of <i>y</i> coordinates
    * @param npoints the total number of points
    */
    private int calculateHeight(int xpoints[], int ypoints[], int npoints) {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++) {
            int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        return boundsMaxY - boundsMinY;
    }

    private void updateGradientAt(java.util.List<Point> points, java.util.List<Integer> radiuses) throws IOException {
        Graphics2D g = imageWithGradient.createGraphics();
        g.drawImage(foreground, 0, 0, null);
        int i = 0;
        final Color white = new Color(255, 255, 255, 255);
        float fractions[] = {0.5f, 0.99f, 1.0f};
        Color colors[] = {
                new Color(0, 0, 0, 255),
                new Color(0, 0, 0, 255),
                new Color(0, 0, 0, 0)
        };
        for (Point point : points) {
            int radius = radiuses.get(i);
            Stroke s = new BasicStroke(15.0f,                      // Width
                    BasicStroke.CAP_SQUARE,    // End cap
                    BasicStroke.JOIN_MITER,    // Join style
                    10.0f,                     // Miter limit

                    generateStrokePattern(), // Dash pattern
                    5.0f);                     // Dash phase
            g.setColor(white);
            g.setPaint(white);
            g.setStroke(s);
            int localRadius = radius + 25;
            g.drawOval(point.x - localRadius, point.y - localRadius, localRadius * 2 + 5, localRadius * 2 + 5);


            RadialGradientPaint paint =
                    new RadialGradientPaint(point, radius, fractions, colors);
            g.setPaint(paint);
            g.setComposite(AlphaComposite.DstOut);

            g.fillOval(point.x - radius, point.y - radius, radius * 2, radius * 2);
            i++;
        }
        g.dispose();
    }

    private float[] generateStrokePattern() {
        final int maxSize = 20;
        float[] result = new float[maxSize];
        Counter.i = 0;
        IntStream
                .generate(() -> ThreadLocalRandom.current().nextInt(50, 200))
                .limit(maxSize)
                .mapToObj(value -> (float) value)
                .forEach(floatVal -> result[Counter.i++] = floatVal);


        return result;
    }

    private BufferedImage convertToARGB(BufferedImage image) {
        BufferedImage newImage =
                new BufferedImage(image.getWidth(), image.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private void saveNewImage(BufferedImage img, File output) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        writer.setOutput(ImageIO.createImageOutputStream(output));
        ImageWriteParam param = writer.getDefaultWriteParam();

        IIOMetadata metadata = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(img), param);
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
        IIOMetadataNode jfif = (IIOMetadataNode) root.getElementsByTagName("app0JFIF").item(0);

        jfif.setAttribute("resUnits", "1");
        jfif.setAttribute("Xdensity", "300");
        jfif.setAttribute("Ydensity", "300");

        metadata.mergeTree(metadata.getNativeMetadataFormatName(), root);

        writer.write(null, new IIOImage(img, null, metadata), param);
        writer.dispose();
    }

    static class Counter {
        static int i = 0;
    }
}

