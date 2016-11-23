package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Landmark;
import com.google.api.services.vision.v1.model.Position;
import com.google.api.services.vision.v1.model.Vertex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.ImageDrawService;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


@Service
@Slf4j
public class ImageDrawServiceImpl implements ImageDrawService {

    private BufferedImage foreground;
    private BufferedImage imageWithGradient;
    private BufferedImage background;
    private Random random = new Random();

    private Color[] gColors = new Color[]{
            new Color(66, 133, 244, 100),
            new Color(219, 68, 55, 100),
            new Color(244, 180, 0, 100),
            new Color(15, 157, 88, 100)};

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

            FaceAnnotation faceAnnotation = visionResponse.getFaceAnnotation();
            Position faceCenterPosition = faceAnnotation
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
                    .get();


            Point point = new Point((int) Math.ceil(faceCenterPosition.getX()),
                    (int) Math.ceil(faceCenterPosition.getY() + pointDownCorrection()));
            updateGradientAt(point, findRadius(faceAnnotation.getBoundingPoly().getVertices()));
            //merge
            graphics.drawImage(imageWithGradient, 0, 0, null);
            String newFilePath = filePath.toString().substring(0, filePath.toString().lastIndexOf(".jpg"));
            final File output = new File(newFilePath + "_fun.jpg");

            saveNewImage(background, output);

        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private int pointDownCorrection() {
        return 0;
    }


    private Color[] shuffleColors(Color[] colorsTable) {
        java.util.List<Color> colorList = Arrays.asList(colorsTable);
        Collections.shuffle(colorList);
        return (Color[]) colorList.toArray();
    }

    private void createForegraundColors(Graphics2D backGraphic, int width, int height) {
        float fractions[] = {0.1f, 0.3f, 0.6f, 0.9f};
        LinearGradientPaint linearGradientPaint =
                new LinearGradientPaint(0,
                        0,
                        width,
                        height,
                        fractions,
                        shuffleColors(gColors),
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);

        backGraphic.setPaint(linearGradientPaint);
        backGraphic.fillRect(0, 0, foreground.getWidth(), foreground.getHeight());
    }

    private Color chooseRandomColor() {
        return gColors[random.nextInt(3)];
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
        return (height + 100) / 2;
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

    private void updateGradientAt(Point point, int radius) throws IOException {
        Graphics2D g = imageWithGradient.createGraphics();
        g.drawImage(foreground, 0, 0, null);

        float fractions[] = {0.0f, 0.95f, 1.0f};
        Color colors[] = {
                new Color(0, 0, 0, 255),
                new Color(0, 0, 0, 255),
                new Color(0, 0, 0, 0)
        };
        RadialGradientPaint paint =
                new RadialGradientPaint(point, radius, fractions, colors);
        g.setPaint(paint);
        g.setComposite(AlphaComposite.DstOut);

        g.fillOval(point.x - radius, point.y - radius, radius * 2, radius * 2);
        g.dispose();
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
}

