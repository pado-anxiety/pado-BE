package com.pado.act.application.validator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("act.emotion-note")
public class EmotionNoteProperties {

    private Situation situation;
    private Thoughts thoughts;
    private Feelings feelings;

    @Getter
    @AllArgsConstructor
    public static class Situation {
        private int length;
    }

    @Getter
    @AllArgsConstructor
    public static class Thoughts {
        private int length;
    }

    @Getter
    @AllArgsConstructor
    public static class Feelings {
        private int length;
    }
}
