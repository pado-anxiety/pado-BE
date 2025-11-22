package com.nyangtodac.auth.service;

import com.nyangtodac.auth.util.CookieUtil;
import com.nyangtodac.auth.util.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CustomStatelessAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_COOKIE_NAME = "OAUTH2_AUTHORIZATION_REQUEST";
    public static final Duration COOKIE_EXPIRY = Duration.ofMinutes(5);

    private final CryptoUtil cryptoUtil;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return loadOAuth2AuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        CookieUtil.addCookie(response, OAUTH2_COOKIE_NAME, cryptoUtil.encrypt(authorizationRequest), COOKIE_EXPIRY);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = loadOAuth2AuthorizationRequest(request);
        CookieUtil.removeCookie(request, response, OAUTH2_COOKIE_NAME);
        return oAuth2AuthorizationRequest;
    }

    public OAuth2AuthorizationRequest loadOAuth2AuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_COOKIE_NAME).map(cryptoUtil::decrypt).orElse(null);
    }


}
