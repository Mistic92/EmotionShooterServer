package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.*;
import pl.lukaszbyjos.emotionshooterserver.domain.EmotionName;
import pl.lukaszbyjos.emotionshooterserver.domain.EmotionStatus;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.VisionService;
import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class VisionServiceStub implements VisionService {
    @Override
    public Observable<VisionResponse> getPhotoData(byte[] bytes) {

        EmotionStatus anger = EmotionStatus
                .builder()
                .name(EmotionName.ANGER)
                .level(1).build();
        EmotionStatus joy = EmotionStatus
                .builder()
                .name(EmotionName.JOY)
                .level(2).build();
        EmotionStatus sorrow = EmotionStatus
                .builder()
                .name(EmotionName.SORROW)
                .level(3).build();
        EmotionStatus suprise = EmotionStatus
                .builder()
                .name(EmotionName.SUPRISE)
                .level(4).build();

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
        v1_2.setX(427);
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
        nose2.setPosition(new Position().setX(900.9714f).setY(1000.562f).setZ(-170.00154f));

        faceAnnotation2.setLandmarks(Arrays.asList(nose2, eyes2));


        return Observable.just(VisionResponse.builder()
                .emotionsList(Arrays.asList(anger, joy, sorrow, suprise))
                .headwear(3)
                .faceAnnotation(Arrays.asList(faceAnnotation1,faceAnnotation2))
                .blur(1)
                .build())
                .delay(1, TimeUnit.SECONDS);
    }
}

