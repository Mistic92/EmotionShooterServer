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

        Landmark eyes = new Landmark();
        eyes.setType("MIDPOINT_BETWEEN_EYES");
        eyes.setPosition(new Position().setX(1011.4305f).setY(1013.3219f).setZ(-73.071327f));

        Landmark nose = new Landmark();
        nose.setType("NOSE_TIP");
        nose.setPosition(new Position().setX(1008.9714f).setY(1222.562f).setZ(-170.00154f));

        return Observable.just(VisionResponse.builder()
                .emotionsList(Arrays.asList(anger, joy, sorrow, suprise))
                .headwear(3)
                .faceAnnotation(new FaceAnnotation()
                        .setAngerLikelihood("NOPE")
                        .setBoundingPoly(boundingPoly)
                        .setFdBoundingPoly(boundingPoly)
                        .setLandmarks(Arrays.asList(eyes, nose)))
                .blur(1)
                .build())
                .delay(1, TimeUnit.SECONDS);
    }
}

