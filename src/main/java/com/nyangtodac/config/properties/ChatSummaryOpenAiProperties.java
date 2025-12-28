package com.nyangtodac.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("summary.openai")
public class ChatSummaryOpenAiProperties {
    private final String model;
    private final int maxTokens;
    private final double temperature;

    public ChatSummaryOpenAiProperties(String model, int maxTokens, double temperature) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }
}
