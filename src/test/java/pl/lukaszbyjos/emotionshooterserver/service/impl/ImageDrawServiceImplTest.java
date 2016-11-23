package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.*;
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
@ContextConfiguration(classes = TestAppContext.class,
        loader = AnnotationConfigContextLoader.class)
public class ImageDrawServiceImplTest {

    @Autowired
    ImageDrawService imageDrawService;

    @Test
    public void createColorfullImage() throws Exception {

        FaceAnnotation faceAnnotation = new FaceAnnotation();

        BoundingPoly boundingPoly = new BoundingPoly();
        Vertex v1 = new Vertex();
        v1.setX(427);
        v1.setY(396);
        Vertex v2 = new Vertex();
        v2.setX(1592);
        v2.setY(396);
        Vertex v3 = new Vertex();
        v3.setX(1592);
        v3.setY(1749);
        Vertex v4 = new Vertex();
        v4.setX(427);
        v4.setY(1749);
        boundingPoly.setVertices(Arrays.asList(v1, v2, v3, v4));
        faceAnnotation.setBoundingPoly(boundingPoly);
        faceAnnotation.setFdBoundingPoly(boundingPoly);

        Landmark eyes = new Landmark();
        eyes.setType("MIDPOINT_BETWEEN_EYES");
        eyes.setPosition(new Position().setX(1011.4305f).setY(1013.3219f).setZ(-73.071327f));

        Landmark nose = new Landmark();
        nose.setType("NOSE_TIP");
        nose.setPosition(new Position().setX(1008.9714f).setY(1222.562f).setZ(-170.00154f));

        faceAnnotation.setLandmarks(Arrays.asList(nose, eyes));
        VisionResponse visionResponse = VisionResponse.builder()
                .faceAnnotation(faceAnnotation)
                .build();
        Path path = new File("hu.jpg").toPath();
        imageDrawService.createColorfullImage(visionResponse, path);
    }

}