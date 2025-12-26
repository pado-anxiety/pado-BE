package com.nyangtodac.auth.infrastructure.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
@Slf4j
public class OAuth2ClientConfig {

    @Bean
    public RestClient.Builder oauth2RestClientBuilder() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(3));
        return RestClient
                .builder()
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    String body = "";
                    try {
                        body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        log.warn("Failed to read OAuth2 4xx response body", ex);
                    }

                    throw new OAuthException("OAuth2 Client error occurred. " + response.getStatusCode(), body);
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    String body = "";
                    try {
                        body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        log.warn("Failed to read OAuth2 5xx response body", ex);
                    }
                    throw new OAuthException("OAuth2 Server error occurred. " + response.getStatusCode(), body);
                });
    }
}
