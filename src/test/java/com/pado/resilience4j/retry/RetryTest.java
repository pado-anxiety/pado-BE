package com.pado.resilience4j.retry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryTest {

    @Test
    @DisplayName("Retry 성공 시 정상 반환")
    void retrySuccessTest() {
        DummyRetryService service = new DummyRetryService();

        //첫 시도 실패하지 않고 바로 성공
        String result = service.call(false);

        assertTrue(result.startsWith("Success"));
    }

    @Test
    @DisplayName("Retry 실패 시 Fallback 반환")
    void retryFailureTest() {
        DummyRetryService service = new DummyRetryService();

        //항상 실패 → 재시도 후 Fallback
        String result = service.call(true);

        assertEquals("Fallback response after 3 attempts", result);
    }
}
