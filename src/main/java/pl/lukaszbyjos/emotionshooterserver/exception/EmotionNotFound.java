package pl.lukaszbyjos.emotionshooterserver.exception;

public class EmotionNotFound extends Exception {
    public EmotionNotFound() {
    }

    public EmotionNotFound(String message) {
        super(message);
    }
}

