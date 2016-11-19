package pl.lukaszbyjos.emotionshooterserver.domain;

public enum Likeness {
    UNKNOWN(0),
    VERY_UNLIKELY(0),
    UNLIKELY(2),
    POSSIBLE(3),
    LIKELY(4),
    VERY_LIKELY(5);

    private final int level;

    Likeness(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

