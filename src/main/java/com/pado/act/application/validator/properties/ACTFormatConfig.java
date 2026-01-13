package com.pado.act.application.validator.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CognitiveDefusionProperties.class, CommittedActionProperties.class})
public class ACTFormatConfig {

}
