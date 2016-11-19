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
    private List<EmotionStatus> emotionsList;
    private String photoUrl;
    private FaceAnnotation faceAnnotation;
//    private EmotionStatus sorrow;
//    private EmotionStatus joy;
//    private EmotionStatus suprise;
//    private EmotionStatus anger;
    private int headwear;
    private int blur;
    private int under_exp;
    private String funText = "";

}

