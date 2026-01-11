package com.pado.chat.quota;

import com.pado.config.properties.ChatQuotaProperties;
import io.github.bucket4j.BucketConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ChatQuotaProperties.class)
public class UserBucketConfig {

    private final ChatQuotaProperties chatQuotaProperties;

    public BucketConfiguration getConfig() {
        return BucketConfiguration
                .builder()
                .addLimit(limit -> limit
                        .capacity(chatQuotaProperties.getMaxTokens())
                        .refillIntervally(
                                chatQuotaProperties.getRefillTokens(),
                                Duration.ofHours(chatQuotaProperties.getRefillIntervalHours())
                        )
                ).build();
    }

}
