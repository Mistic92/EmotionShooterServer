package pl.lukaszbyjos.emotionshooterserver.domain;

public enum EmotionStatus {
    UNKNOWN("Nieznane"),
    VERY_UNLIKELY("Bardzo nieprawdopodobne"),
    UNLIKELY("Nieprawdopodobne"),
    POSSIBLE("Możliwe"),
    LIKELY("Prawdopodobny"),
    VERY_LIKELY("Bardzo prawdopodobne");

    private final String translation;

    EmotionStatus(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}

