package pl.lukaszbyjos.emotionshooterserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisionResponse {
    private String photoUrl;
    private List<FaceAnnotation> annotationList;
}

