package com.pado.auth.infrastructure.oauth.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class KakaoOAuthRevoker {

    private final RestClient restClient;

    @Value("${oauth2.client.registration.kakao.admin-key}")
    private String adminKey;

    public KakaoOAuthRevoker(@Qualifier("oauth2RevokeClientBuilder") RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public void revoke(Long userId, String sub) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", sub);
        try {
            restClient.post()
                    .uri("https://kapi.kakao.com/v1/user/unlink")
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + adminKey)
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
