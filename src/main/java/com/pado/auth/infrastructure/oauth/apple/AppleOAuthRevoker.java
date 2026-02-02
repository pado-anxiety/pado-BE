package com.pado.auth.infrastructure.oauth.apple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class AppleOAuthRevoker {

    private final RestClient restClient;
    private final AppleClientSecretProvider clientSecretProvider;

    @Value("${oauth2.client.registration.apple.client-id}")
    private String clientId;

    public AppleOAuthRevoker(@Qualifier("oauth2RevokeClientBuilder") RestClient.Builder builder, AppleClientSecretProvider clientSecretProvider) {
        this.restClient = builder.build();
        this.clientSecretProvider = clientSecretProvider;
    }

    public void revoke(Long userId, String refreshToken) {
        refreshToken = refreshToken.trim();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecretProvider.createClientSecretToken());
        body.add("token", refreshToken);

        try {
            restClient.post()
                    .uri("https://appleid.apple.com/auth/revoke")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn("Apple Revoke failed, status={}, userId={}, body={}, ", e.getStatusCode(), userId, e.getResponseBodyAsString());
        } catch (Exception e) {
            log.warn("Apple Revoke failed with Exception", e);
        }
    }
}
