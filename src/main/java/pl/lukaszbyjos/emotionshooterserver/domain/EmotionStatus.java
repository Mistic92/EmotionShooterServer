package pl.lukaszbyjos.emotionshooterserver.domain;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class EmotionStatus {
    private EmotionName name;
    private int level;

    public String getName() {
        return name.getName();
    }

    public EmotionName getEmotionName() {
        return name;
    }
}

