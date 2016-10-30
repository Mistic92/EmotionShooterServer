package pl.lukaszbyjos.emotionshooterserver.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @JsonProperty("base")
    private String base64photo;

    @JsonProperty("file")
    private MultipartFile multipartFile;

    @JsonProperty("name")
    private String fileName;


}

