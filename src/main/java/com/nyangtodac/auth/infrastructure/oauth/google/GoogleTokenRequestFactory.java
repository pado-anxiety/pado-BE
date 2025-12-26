package com.nyangtodac.auth.infrastructure.oauth.google;

import com.nyangtodac.auth.infrastructure.oauth.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GoogleTokenRequestFactory {

    @Value("${oauth2.client.registration.google.ios.client-id}")
    private String iosClientId;

    public MultiValueMap<String, String> create(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        if (platform == Platform.IOS) {
            form.add("client_id", iosClientId);
            form.add("code_verifier", codeVerifier);
        } else if (platform == Platform.ANDROID) {

        }
        form.add("redirect_uri", redirectUri);
        form.add("code", authorizationCode);
        return form;
    }
}
