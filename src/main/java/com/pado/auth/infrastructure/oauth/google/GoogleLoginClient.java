package com.pado.auth.infrastructure.oauth.google;

import com.pado.auth.infrastructure.oauth.Platform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleLoginClient {

    private final RestClient restClient;
    private final GoogleTokenRequestFactory tokenRequestFactory;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    public GoogleLoginClient(@Qualifier("oauth2RestClientBuilder") RestClient.Builder builder, GoogleTokenRequestFactory tokenRequestFactory) {
        this.restClient = builder.build();
        this.tokenRequestFactory = tokenRequestFactory;
    }

    public GoogleTokenResponse getToken(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        authorizationCode = authorizationCode.trim();
        codeVerifier = codeVerifier.trim();
        redirectUri = redirectUri.trim();
        return restClient.post()
                .uri(TOKEN_URL)
                .body(tokenRequestFactory.create(authorizationCode, codeVerifier, redirectUri, platform))
                .retrieve()
                .body(GoogleTokenResponse.class);
    }

}
