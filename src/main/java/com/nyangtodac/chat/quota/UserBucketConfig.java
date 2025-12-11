package com.nyangtodac.chat.quota;

import io.github.bucket4j.BucketConfiguration;

import java.time.Duration;

public class UserBucketConfig {

    private static final int MAX_TOKEN = 5;
    private static final int REFILL_TOKEN = 1;
    public static final Duration REFILL_DURATION = Duration.ofHours(2);

    public static BucketConfiguration getConfig() {
        return BucketConfiguration
                .builder()
                .addLimit(limit -> limit
                        .capacity(MAX_TOKEN)
                        .refillIntervally(REFILL_TOKEN, REFILL_DURATION)
                ).build();
    }

}
