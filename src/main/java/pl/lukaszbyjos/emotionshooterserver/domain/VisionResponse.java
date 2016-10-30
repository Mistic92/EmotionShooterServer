package pl.lukaszbyjos.emotionshooterserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisionResponse {
    private String photoUrl;
    private EmotionStatus sorrowLikelihood;
    private EmotionStatus underExposedLikelihood;
    private EmotionStatus surpriseLikelihood;
    private EmotionStatus joyLikelihood;
    private EmotionStatus headwearLikelihood;
    private float detectionConfidence;
    private EmotionStatus blurredLikelihood;
    private EmotionStatus angerLikelihood;

    @Override
    public String toString() {
        return "VisionResponse{" +
                "photoUrl='" + photoUrl + '\'' +
                ", sorrowLikelihood='" + sorrowLikelihood + '\'' +
                ", underExposedLikelihood='" + underExposedLikelihood + '\'' +
                ", surpriseLikelihood='" + surpriseLikelihood + '\'' +
                ", joyLikelihood='" + joyLikelihood + '\'' +
                ", headwearLikelihood='" + headwearLikelihood + '\'' +
                ", detectionConfidence=" + detectionConfidence +
                ", blurredLikelihood='" + blurredLikelihood + '\'' +
                ", angerLikelihood='" + angerLikelihood + '\'' +
                '}';
    }
}

