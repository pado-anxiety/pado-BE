package com.pado.auth.infrastructure.oauth.google;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class GoogleOAuthRevoker {

    private final RestClient restClient;

    public GoogleOAuthRevoker(@Qualifier("oauth2RevokeClientBuilder") RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public void revoke(Long userId, String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", refreshToken);
        try {
            restClient.post()
                    .uri("https://oauth2.googleapis.com/revoke")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn("Kakao Revoke failed, status={}, userId={}, body={}, ", e.getStatusCode(), userId, e.getResponseBodyAsString());
        } catch (Exception e) {
            log.warn("Kakao Revoke failed with Exception", e);
        }
    }
}
