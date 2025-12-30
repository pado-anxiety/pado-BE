package com.nyangtodac.auth.infrastructure.oauth.kakao;

import com.nyangtodac.auth.infrastructure.oauth.UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLoginClient {

    private final RestClient restClient;

    private static final String USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public KakaoLoginClient(@Qualifier("oauth2RestClientBuilder") RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public UserInfo getUserInfo(String accessToken) {
        KakaoUserInfoResponse body = restClient.get()
                .uri(USERINFO_URL)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + accessToken)
                .retrieve()
                .body(KakaoUserInfoResponse.class);
        return new UserInfo(body.getKakaoAccount().getEmail(), body.getKakaoAccount().getProfile().getNickname());
    }
}
