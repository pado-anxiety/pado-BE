package com.pado.act.application.validator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("act.cognitive-defusion")
@Component
public class CognitiveDefusionProperties {

    private int totalTextLength;
}
