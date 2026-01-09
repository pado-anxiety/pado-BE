package com.nyangtodac.openai;

import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequest;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.infrastructure.OpenAiClient;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiRetryConfig;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiServerException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import({OpenAiRetryConfig.class})
public class OpenAiRetryTest {

    private static final String URL = "/v1/chat/completions";

    OpenAiClient openAiClient;

    MockRestServiceServer mockServer;

    @Autowired
    RetryRegistry retryRegistry;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        openAiClient = new OpenAiClient(builder, retryRegistry, circuitBreakerRegistry);
        circuitBreakerRegistry.circuitBreaker("openAiCircuitBreaker").reset();
    }

    @TestConfiguration
    static class TestCircuitBreakerConfig {
        @Bean
        public CircuitBreakerRegistry circuitBreakerRegistry() {
            CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                    .slidingWindowSize(100)
                    .failureRateThreshold(100)
                    .waitDurationInOpenState(Duration.ofSeconds(60))
                    .permittedNumberOfCallsInHalfOpenState(10)
                    .build();

            return CircuitBreakerRegistry.of(config);
        }
    }

    @Test
    @DisplayName("openai 요청 2번 실패 후 1번 성공 응답")
    void retry_should_retry_until_success() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withSuccess("{\"response\":\"ok\"}", MediaType.APPLICATION_JSON));

        ChatCompletionRequest request = new ChatCompletionRequest();

        ChatCompletionResponse response = openAiClient.sendChatRequest(request);

        assertThat(response).isNotNull();

        mockServer.verify();
    }

    @Test
    @DisplayName("openai 요청 3번 모두 실패")
    void retry_should_fail_after_max_attempts() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());

        ChatCompletionRequest request = new ChatCompletionRequest();

        assertThatThrownBy(() -> openAiClient.sendChatRequest(request))
                .isInstanceOf(OpenAiServerException.class);

        mockServer.verify();
    }
}
