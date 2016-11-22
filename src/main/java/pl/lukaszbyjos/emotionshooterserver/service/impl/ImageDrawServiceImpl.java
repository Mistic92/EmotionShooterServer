package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.BoundingPoly;
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
import java.util.List;

@Service
@Slf4j
public class ImageDrawServiceImpl implements ImageDrawService {

    @Override
    public String createColorfullImage(VisionResponse visionResponse, Path filePath) {
        try {
            BoundingPoly boundingPoly = visionResponse.getFaceAnnotation().getBoundingPoly();
            List<Vertex> vertexList = boundingPoly.getVertices();
            BufferedImage img = ImageIO.read(filePath.toFile());
            Graphics2D graphics = img.createGraphics();
            int[] xPos = new int[vertexList.size()];
            int[] yPos = new int[vertexList.size()];
            for (int i = 0; i < vertexList.size(); i++) {
                Vertex v1 = vertexList.get(i);
                xPos[i] = v1.getX();
                yPos[i] = v1.getY();

            }

            graphics.setColor(Color.BLUE);
            graphics.setStroke(new BasicStroke(4));
            Polygon polygon = new Polygon(xPos, yPos, vertexList.size());
//            graphics.drawPolygon(polygon);
            Rectangle r = polygon.getBounds();
            graphics.drawOval((int) r.getX(), (int) r.getY(), r.width, r.height);
            graphics.dispose();

            String newFilePath = filePath.toString().substring(0, filePath.toString().lastIndexOf(".jpg"));
            final File output = new File(newFilePath + "_fun.jpg");
//            ImageIO.write(img, "PNG", output); TOO BIG SIZE o.O

            saveNewImage(img, output);

        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
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

