package pl.lukaszbyjos.emotionshooterserver.service.impl;


import org.springframework.stereotype.Service;
import pl.lukaszbyjos.emotionshooterserver.domain.EmotionName;
import pl.lukaszbyjos.emotionshooterserver.domain.EmotionStatus;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.exception.EmotionNotFound;
import pl.lukaszbyjos.emotionshooterserver.service.FunnyTextService;

import java.util.Comparator;
import java.util.Random;

import static pl.lukaszbyjos.emotionshooterserver.FunnyTextsDictionary.POKER_FACE_TEXTS;
import static pl.lukaszbyjos.emotionshooterserver.FunnyTextsDictionary.getEmotionText;

@Service
public class FunnyTextServiceImpl implements FunnyTextService {
    public static final String EMPTY_STRING = "";

    protected VisionResponse vr;

    @Override
    public String getFunnyText(VisionResponse visionResponse) {
        this.vr = visionResponse;
        if (isPokerFace()) {
            Random random = new Random();
            return POKER_FACE_TEXTS.get(random.nextInt(POKER_FACE_TEXTS.size()));
        } else {
            final EmotionStatus dominatingEmotion;
            try {
                dominatingEmotion = findDominatingEmotion();
                int level = dominatingEmotion.getLevel();
                EmotionName emotionName = dominatingEmotion.getEmotionName();
                return getEmotionText(emotionName, level);
            } catch (EmotionNotFound emotionNotFound) {
                return EMPTY_STRING;
            }
        }

    }

    private boolean isPokerFace() {
        return vr.getEmotionsList()
                .stream()
                .mapToInt(EmotionStatus::getLevel)
                .sum() == 4;
    }

    private EmotionStatus findDominatingEmotion() throws EmotionNotFound {
        return vr.getEmotionsList()
                .stream()
                .max(Comparator.comparingInt(EmotionStatus::getLevel))
                .orElseThrow(EmotionNotFound::new);
    }
}

