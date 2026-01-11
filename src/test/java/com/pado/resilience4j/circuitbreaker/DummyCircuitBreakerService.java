package com.pado.resilience4j.circuitbreaker;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;

import io.github.resilience4j.circuitbreaker.*;

public class DummyCircuitBreakerService {

    private final CircuitBreaker circuitBreaker;

    public DummyCircuitBreakerService() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(1))
                .slidingWindowSize(2)
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(IOException.class)
                .build();

        //2번의 요청에서 (slidingWindowSize)
        //실패율이 50이상일 경우 (failureRateThreshold)
        //CircuitBreaker가 OPEN 된다
        //OPEN되고 요청 없이 1초가 지나면 (waitDurationInOpenState)
        //HALF_OPEN 상태가 된다
        //HALF_OPEN 상태에서 2번의 요청이 성공하면 (permittedNumberOfCallsInHalfOpenState)
        //CircuitBreaker가 CLOSED가 된다
        this.circuitBreaker = CircuitBreaker.of("dummyService", config);
    }

    public String call(boolean fail) {
        Callable<String> callable = CircuitBreaker.decorateCallable(circuitBreaker, () -> {
            if (fail) {
                throw new IOException("Dummy failure");
            }
            return "Success";
        });

        try {
            return callable.call();
        } catch (Exception e) {
            return "Fallback response";
        }
    }


    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }
}
