package pl.lukaszbyjos.emotionshooterserver.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.lukaszbyjos.emotionshooterserver.StorageProperties;

@Configuration
@ComponentScan("pl.lukaszbyjos.emotionshooterserver.service.*")
public class TestAppContext {

    @Bean
    FileSystemStorageService fileSystemStorageService() {
        return new FileSystemStorageService(StorageProperties.builder()
                .location("")
                .build());
    }

}

