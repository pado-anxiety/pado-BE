package com.pado.auth.infrastructure.oauth.apple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class AppleLoginClient {

    private final RestClient restClient;
    private final AppleClientSecretProvider clientSecretProvider;

    @Value("${oauth2.client.registration.apple.client-id}")
    private String clientId;

    public AppleLoginClient(@Qualifier("oauth2RestClientBuilder") RestClient.Builder builder, AppleClientSecretProvider clientSecretProvider) {
        this.restClient = builder.build();
        this.clientSecretProvider = clientSecretProvider;
    }

    public AppleTokenResponse getTokenResponse(String authorizationCode) {
        authorizationCode = authorizationCode.trim();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecretProvider.createClientSecretToken());
        body.add("code", authorizationCode);

        return restClient.post()
                .uri("https://appleid.apple.com/auth/token")
                .body(body)
                .retrieve()
                .body(AppleTokenResponse.class);
    }
}
