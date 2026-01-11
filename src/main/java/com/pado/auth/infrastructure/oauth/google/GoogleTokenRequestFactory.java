package com.pado.auth.infrastructure.oauth.google;

import com.pado.auth.infrastructure.oauth.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GoogleTokenRequestFactory {

    @Value("${oauth2.client.registration.google.ios.client-id}")
    private String iosClientId;

    @Value("${oauth2.client.registration.google.android.client-id}")
    private String androidClientId;

    @Value("${oauth2.client.registration.google.android.client-secret}")
    private String androidClientSecret;

    public MultiValueMap<String, String> create(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        if (platform == Platform.IOS) {
            form.add("client_id", iosClientId);
            form.add("code_verifier", codeVerifier);
        } else if (platform == Platform.ANDROID) {
            form.add("client_id", androidClientId);
            form.add("client_secret", androidClientSecret);
        }
        form.add("redirect_uri", redirectUri);
        form.add("code", authorizationCode);
        return form;
    }
}
