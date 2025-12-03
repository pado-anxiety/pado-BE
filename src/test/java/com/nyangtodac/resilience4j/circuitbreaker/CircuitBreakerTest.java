package com.nyangtodac.resilience4j.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircuitBreakerTest {

    @Test
    @DisplayName("CircuitBreaker 테스트 OPEN -> HALF_OPEN -> CLOSED")
    void circuitBreaker1() throws InterruptedException {
        DummyCircuitBreakerService service = new DummyCircuitBreakerService();
        CircuitBreaker breaker = service.getCircuitBreaker();

        //첫 호출 실패 → Fallback
        String result1;
        try {
            result1 = service.call(true);
        } catch (Exception e) {
            result1 = "Fallback response";
        }
        assertEquals("Fallback response", result1);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());

        //두 번째 호출 실패 → OPEN
        String result2;
        try {
            result2 = service.call(true);
        } catch (Exception e) {
            result2 = "Fallback response";
        }
        assertEquals("Fallback response", result2);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        //OPEN 상태에서 호출 → 즉시 Fallback
        String result3;
        try {
            result3 = service.call(false);
        } catch (Exception e) {
            result3 = "Fallback response";
        }
        assertEquals("Fallback response", result3);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        //HALF_OPEN을 위해 시간 대기
        Thread.sleep(1100);

        String result4;
        try {
            result4 = service.call(false);
        } catch (Exception e) {
            result4 = "Fallback response";
        }
        assertEquals("Success", result4);
        assertEquals(CircuitBreaker.State.HALF_OPEN, breaker.getState());

        //두번 요청에 성공했으므로 CircuitBreaker CLOSED
        String result5;
        try {
            result5 = service.call(false);
        } catch (Exception e) {
            result5 = "Fallback response";
        }
        assertEquals("Success", result5);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());

        String result6;
        try {
            result6 = service.call(false);
        } catch (Exception e) {
            result6 = "Fallback response";
        }
        assertEquals("Success", result6);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());

        String result7;
        try {
            result7 = service.call(false);
        } catch (Exception e) {
            result7 = "Fallback response";
        }
        assertEquals("Success", result7);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());
    }

    @Test
    @DisplayName("CircuitBreaker 테스트 OPEN -> HALF_OPEN -> OPEN 유지")
    void circuitBreaker2() throws InterruptedException {
        DummyCircuitBreakerService service = new DummyCircuitBreakerService();
        CircuitBreaker breaker = service.getCircuitBreaker();

        //첫 호출 실패 → Fallback
        String result1;
        try {
            result1 = service.call(true);
        } catch (Exception e) {
            result1 = "Fallback response";
        }
        assertEquals("Fallback response", result1);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());

        //두 번째 호출 실패 → OPEN
        String result2;
        try {
            result2 = service.call(true);
        } catch (Exception e) {
            result2 = "Fallback response";
        }
        assertEquals("Fallback response", result2);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        //OPEN 상태에서 호출 → 즉시 Fallback
        String result3;
        try {
            result3 = service.call(false);
        } catch (Exception e) {
            result3 = "Fallback response";
        }
        assertEquals("Fallback response", result3);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        //HALF_OPEN을 위해 시간 대기
        Thread.sleep(1100);

        String result4;
        try {
            result4 = service.call(true);
        } catch (Exception e) {
            result4 = "Fallback response";
        }
        assertEquals("Fallback response", result4);
        assertEquals(CircuitBreaker.State.HALF_OPEN, breaker.getState());

        //HALF_OPEN 상태에서 2번 실패했으므로 다시 OPEN
        String result5;
        try {
            result5 = service.call(true);
        } catch (Exception e) {
            result5 = "Fallback response";
        }
        assertEquals("Fallback response", result5);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        String result6;
        try {
            result6 = service.call(true);
        } catch (Exception e) {
            result6 = "Fallback response";
        }
        assertEquals("Fallback response", result6);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());

        String result7;
        try {
            result7 = service.call(true);
        } catch (Exception e) {
            result7 = "Fallback response";
        }
        assertEquals("Fallback response", result7);
        assertEquals(CircuitBreaker.State.OPEN, breaker.getState());
    }
}
