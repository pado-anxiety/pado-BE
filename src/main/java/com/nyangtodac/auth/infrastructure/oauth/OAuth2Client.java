package com.nyangtodac.auth.infrastructure.oauth;

import com.nyangtodac.user.domain.LoginType;

public interface OAuth2Client {

    String getAccessToken(String authorizationCode, String codeVerifier, String redirectUri, Platform platform);

    UserInfo getUserInfo(String accessToken);

    LoginType getType();
}
