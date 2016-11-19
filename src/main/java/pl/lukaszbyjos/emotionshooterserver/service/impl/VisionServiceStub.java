package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.services.vision.v1.model.FaceAnnotation;
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


        return Observable.just(VisionResponse.builder()
                .emotionsList(Arrays.asList(anger, joy, sorrow, suprise))
                .headwear(3)
                .faceAnnotation(new FaceAnnotation().setAngerLikelihood("NOPE"))
                .blur(1)
                .build())
                .delay(2, TimeUnit.SECONDS);
    }
}

