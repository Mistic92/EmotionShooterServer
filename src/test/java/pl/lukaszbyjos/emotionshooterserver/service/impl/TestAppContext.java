package pl.lukaszbyjos.emotionshooterserver.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import pl.lukaszbyjos.emotionshooterserver.StorageProperties;

import java.util.Properties;

@Configuration
@ComponentScan("pl.lukaszbyjos.emotionshooterserver.service.*")
public class TestAppContext {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        properties.setProperty("effects.max-colors", "2");

        pspc.setProperties(properties);
        return pspc;
    }

    @Bean
    FileSystemStorageService fileSystemStorageService() {
        return new FileSystemStorageService(StorageProperties.builder()
                .location("")
                .build());
    }

}

