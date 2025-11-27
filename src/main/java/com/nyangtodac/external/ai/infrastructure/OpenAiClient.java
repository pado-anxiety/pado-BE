package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.OpenAiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.Duration;

@Component
public class OpenAiClient {

    private static final String CHAT_COMPLETION_URL = "/v1/chat/completions";
    private final RestClient restClient;

    public OpenAiClient(@Value("${openai.client.apikey}") String apiKey) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ChatCompletionResponse sendRequest(ChatCompletionRequest request) {
        try {
            return restClient.post()
                    .uri(CHAT_COMPLETION_URL)
                    .body(request)
                    .retrieve()
                    .body(ChatCompletionResponse.class);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new OpenAiException("AI timeout", e);
            }
            if (e.getCause() instanceof ConnectException) {
                throw new OpenAiException("AI server unavailable", e);
            }
            throw new OpenAiException(e);
        } catch (HttpServerErrorException e) {
            throw new OpenAiException("AI server error", e);
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new OpenAiException("AI rate limit exceeded", e);
        } catch (HttpClientErrorException e) {
            throw new OpenAiException("Unexpected RestClient error", e);
        } catch (HttpStatusCodeException e) {
            throw new OpenAiException("Unexpected AI HTTP error" + " HttpStatusCode: " + e.getStatusCode(), e);
        }
    }
}
