package pl.lukaszbyjos.emotionshooterserver.domain;

import lombok.*;

@EqualsAndHashCode
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPhotoInfo {
    private String photoUrl;
}

