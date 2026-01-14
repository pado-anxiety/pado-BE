package com.pado.act.application.validator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("act.committed-action")
public class CommittedActionProperties {

    private Diagnosis diagnosis;
    private Value value;
    private Barrier barrier;
    private Action action;

    @AllArgsConstructor
    @Getter
    public static class Diagnosis {
        private int min;
        private int max;
    }

    @AllArgsConstructor
    @Getter
    public static class Value {
        private int length;
    }

    @AllArgsConstructor
    @Getter
    public static class Barrier {
        private int length;
    }

    @AllArgsConstructor
    @Getter
    public static class Action {
        private int length;
    }
}
