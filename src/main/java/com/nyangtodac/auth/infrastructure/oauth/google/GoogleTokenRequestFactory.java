package com.nyangtodac.auth.infrastructure.oauth.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GoogleTokenRequestFactory {

    @Value("${oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public MultiValueMap<String, String> create(String authorizationCode, String codeVerifier) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("code", authorizationCode);
        form.add("code_verifier", codeVerifier);
        return form;
    }
}
