package pl.lukaszbyjos.emotionshooterserver.service;

import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;

import java.nio.file.Path;

public interface ImageDrawService {

    String createColorfullImage(VisionResponse visionResponse, Path filePath);
}

