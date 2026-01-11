package com.pado.auth.controller.dto;

import com.pado.auth.infrastructure.oauth.Platform;
import lombok.Getter;

@Getter
public class GoogleLoginRequest {

    private String codeVerifier;
    private String authorizationCode;
    private String redirectUri;
    private Platform platform;
}
