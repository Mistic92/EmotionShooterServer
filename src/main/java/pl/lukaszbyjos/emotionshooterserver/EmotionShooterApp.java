package pl.lukaszbyjos.emotionshooterserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pl.lukaszbyjos.emotionshooterserver.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class EmotionShooterApp implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EmotionShooterApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}

