package com.pado.act.application.validator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("act.acceptance")
public class AcceptanceProperties {

    private BreathingTime breathingTime;

    @Getter
    @AllArgsConstructor
    public static class BreathingTime {
        private int min;
    }
}
