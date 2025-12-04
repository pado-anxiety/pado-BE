package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.retry.OpenAiNonRetryableException;
import com.nyangtodac.external.ai.retry.OpenAiRetryableException;
import com.nyangtodac.external.ai.retry.OpenAiClientException;
import com.nyangtodac.external.ai.retry.OpenAiServerException;
import io.github.resilience4j.retry.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class OpenAiClient {

    private static final String CHAT_COMPLETION_URL = "/v1/chat/completions";

    private final RestClient restClient;
    private final Retry retry;

    public OpenAiClient(Retry retry, RestClient.Builder builder) {
        this.restClient = builder.build();
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
                throw new OpenAiServerException("Unexpected 5xx error" + " HttpStatusCode: " + e.getStatusCode(), e)
            }
            throw new OpenAiClientException("Unexpected 4xx error" + " HttpStatusCode: " + e.getStatusCode(), e);
        }
    }
}
