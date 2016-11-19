package pl.lukaszbyjos.emotionshooterserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lukaszbyjos.emotionshooterserver.service.VisionService;
import pl.lukaszbyjos.emotionshooterserver.service.impl.VisionServiceImpl;
import pl.lukaszbyjos.emotionshooterserver.service.impl.VisionServiceStub;

@Configuration
public class VisionServiceConfig {

    @Value("${vision.enabled}")
    private boolean visionEnabled;

    @Bean
    public VisionService provideVisionService() {
        if (visionEnabled)
            return new VisionServiceImpl();
        else {
            return new VisionServiceStub();
        }
    }
}

