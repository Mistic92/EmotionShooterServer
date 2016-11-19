package pl.lukaszbyjos.emotionshooterserver.service;

import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;

public interface FunnyTextService {

    String getFunnyText(VisionResponse visionResponse);
}
