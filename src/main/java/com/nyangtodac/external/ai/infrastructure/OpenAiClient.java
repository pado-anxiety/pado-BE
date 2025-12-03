package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.retry.OpenAiNonRetryableException;
import com.nyangtodac.external.ai.retry.OpenAiRetryableException;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.List;

@Component
public class OpenAiClient {

    private static final String CHAT_COMPLETION_URL = "/v1/chat/completions";
    private final RestClient restClient;
    private final Retry retry;

    public OpenAiClient(
            @Value("${openai.client.apikey}") String apiKey,
            Retry retry
    ) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.retry = retry;
    }

    public ChatCompletionResponse sendRequest(ChatCompletionRequest request) {
        return Retry.decorateSupplier(retry, () -> doRequest(request)).get();
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
                throw new OpenAiRetryableException("AI timeout", e);
            }
            if (e.getCause() instanceof ConnectException) {
                throw new OpenAiRetryableException("AI server unavailable", e);
            }
            throw new OpenAiRetryableException(e);
        } catch (HttpServerErrorException e) {
            throw new OpenAiRetryableException("AI server error", e);
        } catch (HttpClientErrorException.TooManyRequests e) {
            StringBuilder messageBuilder = new StringBuilder("AI rate limit exceeded");

            List<String> retryAfterHeader = e.getResponseHeaders().get("Retry-After");
            if (!retryAfterHeader.isEmpty()) {
                messageBuilder.append(". Retry after: ").append(retryAfterHeader.get(0)).append(" seconds");
            }

            throw new OpenAiNonRetryableException(messageBuilder.toString(), e);
        } catch (HttpClientErrorException e) {
            throw new OpenAiNonRetryableException("Unexpected RestClient error", e);
        } catch (HttpStatusCodeException e) {
            throw new OpenAiNonRetryableException("Unexpected AI HTTP error" + " HttpStatusCode: " + e.getStatusCode(), e);
        }
    }
}
