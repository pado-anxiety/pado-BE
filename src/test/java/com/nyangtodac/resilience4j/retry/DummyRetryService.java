package com.nyangtodac.resilience4j.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyRetryService {

    private final Retry retry;

    public DummyRetryService() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(200))
                .retryExceptions(IOException.class)
                .build();

        retry = Retry.of("dummyRetryService", config);
    }

    public String call(boolean fail) {
        AtomicInteger attemptCounter = new AtomicInteger(0);
        Callable<String> decoratedCallable = Retry.decorateCallable(retry, () -> {
            attemptCounter.incrementAndGet();
            if (fail) {
                throw new IOException("Simulated failure");
            }
            return "Success on attempt " + attemptCounter.get();
        });

        try {
            return decoratedCallable.call();
        } catch (Exception e) {
            return "Fallback response after " + attemptCounter.get() + " attempts";
        }
    }
}
