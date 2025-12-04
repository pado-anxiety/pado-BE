package com.nyangtodac.external.ai.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAiRetryConfig {

    @Bean
    public Retry openAiRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(200))
                .retryExceptions(OpenAiServerException.class)
                .build();
        return Retry.of("openAiRetry", config);
    }

}
