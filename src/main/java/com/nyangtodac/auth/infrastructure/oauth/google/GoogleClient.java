package com.nyangtodac.auth.infrastructure.oauth.google;

import com.nyangtodac.auth.infrastructure.oauth.OAuth2Client;
import com.nyangtodac.auth.infrastructure.oauth.UserInfo;
import com.nyangtodac.user.domain.LoginType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleClient implements OAuth2Client {

    private final RestClient restClient;
    private final GoogleTokenRequestFactory tokenRequestFactory;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URL = "https://openidconnect.googleapis.com/v1/userinfo";

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public GoogleClient(@Qualifier("oauth2RestClientBuilder") RestClient.Builder builder, GoogleTokenRequestFactory tokenRequestFactory) {
        this.restClient = builder.build();
        this.tokenRequestFactory = tokenRequestFactory;
    }

    @Override
    public String getAccessToken(String authorizationCode, String codeVerifier) {
        GoogleTokenResponse body = restClient.post()
                .uri(TOKEN_URL)
                .body(tokenRequestFactory.create(authorizationCode, codeVerifier))
                .retrieve()
                .body(GoogleTokenResponse.class);
        return body.getAccessToken();
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        GoogleUserInfoResponse body = restClient.get()
                .uri(USERINFO_URL)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + accessToken)
                .retrieve()
                .body(GoogleUserInfoResponse.class);
        return new UserInfo(body.getEmail(), body.getName());
    }

    @Override
    public LoginType getType() {
        return LoginType.GOOGLE;
    }
}
