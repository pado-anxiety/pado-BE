package com.pado.diary.application.validator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("diary")
public class DiaryProperties {

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
        private int max;
    }
}

