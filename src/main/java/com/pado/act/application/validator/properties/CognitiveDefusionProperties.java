package com.pado.act.application.validator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("act.cognitive-defusion")
public class CognitiveDefusionProperties {

    private int totalTextLength;
}
