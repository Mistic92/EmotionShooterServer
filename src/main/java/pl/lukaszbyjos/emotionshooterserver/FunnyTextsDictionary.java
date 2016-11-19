package pl.lukaszbyjos.emotionshooterserver;

import pl.lukaszbyjos.emotionshooterserver.domain.EmotionName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FunnyTextsDictionary {
    public static final List<String> POKER_FACE_TEXTS = Arrays.asList("PPPPOKER FACE", "Grywasz w pokera? Czas zacząć!");
    private static HashMap<Integer, List<String>> ANGER_TEXTS = new HashMap<>();
    private static HashMap<Integer, List<String>> JOY_TEXTS = new HashMap<>();
    private static HashMap<Integer, List<String>> SORROW_TEXTS = new HashMap<>();
    private static HashMap<Integer, List<String>> SUPRISE_TEXTS = new HashMap<>();

    static {
        ANGER_TEXTS.put(2, Arrays.asList("Może melisy?"));
        ANGER_TEXTS.put(3, Arrays.asList("Może melisy?"));
        ANGER_TEXTS.put(4, Arrays.asList("Może melisy?"));
        ANGER_TEXTS.put(5, Arrays.asList("Może melisy?"));

        JOY_TEXTS.put(2, Arrays.asList("Jak przejść do historii?..... CTRL+H", "Harry Potter i Maska Podsieci"));
        JOY_TEXTS.put(3, Arrays.asList("Jak przejść do historii?..... CTRL+H", "Harry Potter i Maska Podsieci"));
        JOY_TEXTS.put(4, Arrays.asList("Jak przejść do historii?..... CTRL+H", "Harry Potter i Maska Podsieci"));
        JOY_TEXTS.put(5, Arrays.asList("TAK TRZYMAJ! :)", "DOBRA ROBOTA! :)"));

        SORROW_TEXTS.put(2, Arrays.asList("Jak przejść do historii?..... CTRL+H", "Harry Potter i Maska Podsieci"));
        SORROW_TEXTS.put(3, Arrays.asList("Jak przejść do historii?..... CTRL+H", "Harry Potter i Maska Podsieci"));
        SORROW_TEXTS.put(4, Arrays.asList("Uśmiechnij się!"));
        SORROW_TEXTS.put(5, Arrays.asList("Wszystko w porządku?"));

        SUPRISE_TEXTS.put(2, Arrays.asList("That eyes!"));
        SUPRISE_TEXTS.put(3, Arrays.asList("That eyes!"));
        SUPRISE_TEXTS.put(4, Arrays.asList("That eyes!"));
        SUPRISE_TEXTS.put(5, Arrays.asList("That eyes!"));
    }

    public static String getEmotionText(EmotionName emotionName, int level) {
        Random random = new Random();
        String text = "";
        switch (emotionName) {
            case JOY: {
                List<String> textList = JOY_TEXTS.get(level);
                text = textList.get(random.nextInt(textList.size()));
                break;
            }
            case SORROW: {
                List<String> textList = SORROW_TEXTS.get(level);
                text = textList.get(random.nextInt(textList.size()));
                break;
            }
            case ANGER: {
                List<String> textList = ANGER_TEXTS.get(level);
                text = textList.get(random.nextInt(textList.size()));
                break;
            }
            case SUPRISE: {
                List<String> textList = SUPRISE_TEXTS.get(level);
                text = textList.get(random.nextInt(textList.size()));
                break;
            }
        }
        return text;
    }
}

