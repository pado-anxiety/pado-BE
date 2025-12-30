package com.nyangtodac.auth.infrastructure.oauth.google;

import com.nyangtodac.auth.infrastructure.oauth.Platform;
import com.nyangtodac.auth.infrastructure.oauth.UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleLoginClient {

    private final RestClient restClient;
    private final GoogleTokenRequestFactory tokenRequestFactory;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URL = "https://openidconnect.googleapis.com/v1/userinfo";

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public GoogleLoginClient(@Qualifier("oauth2RestClientBuilder") RestClient.Builder builder, GoogleTokenRequestFactory tokenRequestFactory) {
        this.restClient = builder.build();
        this.tokenRequestFactory = tokenRequestFactory;
    }

    public String getAccessToken(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        GoogleTokenResponse body = restClient.post()
                .uri(TOKEN_URL)
                .body(tokenRequestFactory.create(authorizationCode, codeVerifier, redirectUri, platform))
                .retrieve()
                .body(GoogleTokenResponse.class);
        return body.getAccessToken();
    }


    public UserInfo getUserInfo(String accessToken) {
        GoogleUserInfoResponse body = restClient.get()
                .uri(USERINFO_URL)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + accessToken)
                .retrieve()
                .body(GoogleUserInfoResponse.class);
        return new UserInfo(body.getEmail(), body.getName());
    }

}
