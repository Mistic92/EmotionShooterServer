package pl.lukaszbyjos.emotionshooterserver.service;

import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;

public interface ResponsesBackupService {
    void saveResponse(VisionResponse visionResponse);
}
