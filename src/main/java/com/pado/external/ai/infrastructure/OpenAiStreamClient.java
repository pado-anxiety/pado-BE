package com.pado.external.ai.infrastructure;

import com.pado.external.ai.resilience4j.retry.OpenAiClientException;
import com.pado.external.ai.resilience4j.retry.OpenAiServerException;
import io.micrometer.core.annotation.Counted;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class OpenAiStreamClient {

    private static final String CHAT_COMPLETION_URL = "/v1/chat/completions";

    private final WebClient webClient;

    public OpenAiStreamClient(@Qualifier("openAiWebClientBuilder") WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Counted("ai.chat.stream")
    public Flux<String> sendChatRequest(ChatCompletionStreamRequest request) {
        return doRequest(request);
    }

    private Flux<String> doRequest(ChatCompletionStreamRequest request) {
        return webClient.post()
                .uri(CHAT_COMPLETION_URL)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .filter(sse -> sse.data() != null)
                .filter(sse -> !"[DONE]".equals(sse.data()))
                .mapNotNull(ServerSentEvent::data)
                .onErrorMap(this::mapException);
    }

    private Throwable mapException(Throwable e) {
        if (e instanceof WebClientRequestException requestEx) {
            if (requestEx.getCause() instanceof SocketTimeoutException) {
                return new OpenAiServerException("AI timeout", requestEx);
            }
            if (requestEx.getCause() instanceof ConnectException) {
                return new OpenAiServerException("AI server unavailable", requestEx);
            }
            return new OpenAiServerException(requestEx);
        }
        if (e instanceof WebClientResponseException responseEx) {
            if (responseEx.getStatusCode().is5xxServerError()) {
                return new OpenAiServerException(
                        "AI server error HttpStatusCode: " + responseEx.getStatusCode(), responseEx
                );
            }
            if (responseEx.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                StringBuilder msg = new StringBuilder("AI rate limit exceeded");
                List<String> retryAfter = responseEx.getHeaders().get("Retry-After");
                if (retryAfter != null && !retryAfter.isEmpty()) {
                    msg.append(". Retry after: ").append(retryAfter.get(0)).append(" seconds");
                }
                return new OpenAiClientException(msg.toString(), responseEx);
            }
            if (responseEx.getStatusCode().is4xxClientError()) {
                return new OpenAiClientException(
                        "Unexpected 4xx error HttpStatusCode: " + responseEx.getStatusCode(), responseEx
                );
            }
        }
        return new OpenAiServerException("Unexpected error", e);
    }
}
