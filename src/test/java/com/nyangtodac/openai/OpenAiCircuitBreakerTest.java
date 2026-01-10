package com.nyangtodac.openai;

import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequest;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.infrastructure.OpenAiClient;
import com.nyangtodac.external.ai.resilience4j.circuitbreaker.OpenAiCircuitBreakerConfig;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiRetryConfig;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiServerException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import({OpenAiRetryConfig.class, OpenAiCircuitBreakerConfig.class})
public class OpenAiCircuitBreakerTest {

    private static final String URL = "/v1/chat/completions";

    OpenAiClient openAiClient;

    MockRestServiceServer mockServer;

    @Autowired
    RetryRegistry retryRegistry;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        openAiClient = new OpenAiClient(builder, retryRegistry, circuitBreakerRegistry);
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("openAiCircuitBreaker");
        circuitBreaker.reset();
    }
    @Test
    @DisplayName("retry -> circuit Breaker 순서대로 작동하므로, 여러번 실패 후 성공하면 circuit Breaker에는 성공으로 기록된다")
    void circuitBreakerTest1() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withSuccess("{\"response\":\"ok\"}", MediaType.APPLICATION_JSON));

        ChatCompletionRequest request = new ChatCompletionRequest();

        ChatCompletionResponse response = openAiClient.sendChatRequest(request);

        assertThat(response).isNotNull();
        assertThat(circuitBreaker.getMetrics().getNumberOfFailedCalls()).isEqualTo(0);
        assertThat(circuitBreaker.getMetrics().getNumberOfSuccessfulCalls()).isEqualTo(1);

        mockServer.verify();
    }

    @Test
    @DisplayName("최종적으로 retry가 실패하면 circuit Breaker에는 실패로 기록된다")
    void circuitBreakerTest2() {
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());
        mockServer.expect(requestTo(URL))
                .andRespond(withServerError());

        ChatCompletionRequest request = new ChatCompletionRequest();

        assertThatThrownBy(() -> openAiClient.sendChatRequest(request))
                .isInstanceOf(OpenAiServerException.class);
        assertThat(circuitBreaker.getMetrics().getNumberOfFailedCalls()).isEqualTo(1);
        assertThat(circuitBreaker.getMetrics().getNumberOfSuccessfulCalls()).isEqualTo(0);

        mockServer.verify();
    }
}
