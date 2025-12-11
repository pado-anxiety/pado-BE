package com.nyangtodac.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("chat.quota")
@Getter
public class ChatQuotaProperties {
    private final int maxTokens;
    private final int refillIntervalHours;
    private final int refillTokens;

    public ChatQuotaProperties(int maxTokens, int refillIntervalHours, int refillTokens) {
        this.maxTokens = maxTokens;
        this.refillIntervalHours = refillIntervalHours;
        this.refillTokens = refillTokens;
    }
}
