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

        FaceAnnotation faceAnnotation1 = new FaceAnnotation();

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
        faceAnnotation1.setBoundingPoly(boundingPoly);
        faceAnnotation1.setFdBoundingPoly(boundingPoly);

        Landmark eyes = new Landmark();
        eyes.setType("MIDPOINT_BETWEEN_EYES");
        eyes.setPosition(new Position().setX(1011.4305f).setY(1013.3219f).setZ(-73.071327f));

        Landmark nose = new Landmark();
        nose.setType("NOSE_TIP");
        nose.setPosition(new Position().setX(1008.9714f).setY(1222.562f).setZ(-170.00154f));

        faceAnnotation1.setLandmarks(Arrays.asList(nose, eyes));
//
        FaceAnnotation faceAnnotation2 = new FaceAnnotation();

        BoundingPoly boundingPoly2 = new BoundingPoly();
        Vertex v1_2 = new Vertex();
        v1_2.setX(200);
        v1_2.setY(396);
        Vertex v2_2 = new Vertex();
        v2_2.setX(1592);
        v2_2.setY(396);
        Vertex v3_2 = new Vertex();
        v3_2.setX(1592);
        v3_2.setY(1749);
        Vertex v4_2 = new Vertex();
        v4_2.setX(427);
        v4_2.setY(1749);
        boundingPoly2.setVertices(Arrays.asList(v1_2, v2_2, v3_2, v4_2));
        faceAnnotation2.setBoundingPoly(boundingPoly2);
        faceAnnotation2.setFdBoundingPoly(boundingPoly2);

        Landmark eyes2 = new Landmark();
        eyes2.setType("MIDPOINT_BETWEEN_EYES");
        eyes2.setPosition(new Position().setX(1011.4305f).setY(1013.3219f).setZ(-73.071327f));

        Landmark nose2 = new Landmark();
        nose2.setType("NOSE_TIP");
        nose2.setPosition(new Position().setX(1008.9714f).setY(1222.562f).setZ(-170.00154f));

        faceAnnotation2.setLandmarks(Arrays.asList(nose2, eyes2));

        VisionResponse visionResponse = VisionResponse.builder()
                .faceAnnotation(Arrays.asList(faceAnnotation1, faceAnnotation2))
                .build();
        Path path = new File("hu.jpg").toPath();
        imageDrawService.createColorfullImage(visionResponse, path);
    }

}