package pl.lukaszbyjos.emotionshooterserver.websocket;

import lombok.*;

@EqualsAndHashCode
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingInfo {
    private boolean processing;
}

