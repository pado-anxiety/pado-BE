package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.resilience4j.retry.OpenAiClientException;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiServerException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.annotation.Counted;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.function.Supplier;

@Component
public class OpenAiClient {

    private static final String CHAT_COMPLETION_URL = "/v1/chat/completions";

    private final RestClient restClient;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;

    public OpenAiClient(@Qualifier("openAiRestClientBuilder") RestClient.Builder builder, RetryRegistry retryRegistry, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.restClient = builder.build();
        this.retry = retryRegistry.retry("openAiRetry");
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("openAiCircuitBreaker");
    }

    @Counted("ai.chat")
    public ChatCompletionResponse sendChatRequest(ChatCompletionRequest request) {
        Supplier<ChatCompletionResponse> supplier = () -> doRequest(request);
        supplier = Retry.decorateSupplier(retry, supplier);
        supplier = CircuitBreaker.decorateSupplier(circuitBreaker, supplier);
        return supplier.get();
    }

    @Counted("ai.summary")
    public ChatCompletionResponse sendSummaryRequest(ChatCompletionRequest request) {
        return doRequest(request);
    }

    @Counted("ai.act.recommend")
    public ChatCompletionResponse sendACTRecommendRequest(ChatCompletionRequest request) {
        return doRequest(request);
    }

    private ChatCompletionResponse doRequest(ChatCompletionRequest request) {
        try {
            return restClient.post()
                    .uri(CHAT_COMPLETION_URL)
                    .body(request)
                    .retrieve()
                    .body(ChatCompletionResponse.class);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new OpenAiServerException("AI timeout", e);
            }
            if (e.getCause() instanceof ConnectException) {
                throw new OpenAiServerException("AI server unavailable", e);
            }
            throw new OpenAiServerException(e);
        } catch (HttpServerErrorException e) {
            throw new OpenAiServerException("AI server error", e);
        } catch (HttpClientErrorException.TooManyRequests e) {
            StringBuilder messageBuilder = new StringBuilder("AI rate limit exceeded");

            List<String> retryAfterHeader = e.getResponseHeaders().get("Retry-After");
            if (!retryAfterHeader.isEmpty()) {
                messageBuilder.append(". Retry after: ").append(retryAfterHeader.get(0)).append(" seconds");
            }

            throw new OpenAiClientException(messageBuilder.toString(), e);
        } catch (HttpClientErrorException e) {
            throw new OpenAiClientException("Unexpected RestClient error", e);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new OpenAiServerException("Unexpected 5xx error" + " HttpStatusCode: " + e.getStatusCode(), e);
            }
            throw new OpenAiClientException("Unexpected 4xx error" + " HttpStatusCode: " + e.getStatusCode(), e);
        }
    }
}
