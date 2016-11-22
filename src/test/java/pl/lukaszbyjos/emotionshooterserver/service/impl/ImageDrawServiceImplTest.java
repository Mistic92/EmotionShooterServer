package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.BoundingPoly;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Vertex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.ImageDrawService;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestAppContext.class,
        loader=AnnotationConfigContextLoader.class)
public class ImageDrawServiceImplTest {

    @Autowired
    ImageDrawService imageDrawService;

    @Test
    public void createColorfullImage() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] buff = Files.readAllBytes(new File("testFaceAnn").toPath());

        FaceAnnotation faceAnnotation = new FaceAnnotation();
        BoundingPoly boundingPoly = new BoundingPoly();
        Vertex v1 = new Vertex();
        v1.setX(229);
        v1.setY(417);
        Vertex v2 = new Vertex();
        v2.setX(798);
        v2.setY(417);
        Vertex v3 = new Vertex();
        v3.setX(798);
        v3.setY(986);
        Vertex v4 = new Vertex();
        v4.setX(229);
        v4.setY(986);
        boundingPoly.setVertices(Arrays.asList(v1, v2, v3, v4));
        faceAnnotation.setBoundingPoly(boundingPoly);
        faceAnnotation.setFdBoundingPoly(boundingPoly);
        VisionResponse visionResponse = VisionResponse.builder()
                .faceAnnotation(faceAnnotation)
                .build();
        Path path = new File("H:\\ProgramowanieProjekty\\EmotionShooterServer\\upload-dir\\JPEG_20161118_091046_-12392496101811091051.jpg").toPath();
        imageDrawService.createColorfullImage(visionResponse, path);
    }

}