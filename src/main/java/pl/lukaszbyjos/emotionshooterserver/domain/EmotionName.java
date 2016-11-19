package pl.lukaszbyjos.emotionshooterserver.domain;

public enum EmotionName {
    JOY("Radość"),
    SORROW("Smutek"),
    ANGER("Złość"),
    SUPRISE("Zaskoczenie");

    private final String name;

    EmotionName(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}

