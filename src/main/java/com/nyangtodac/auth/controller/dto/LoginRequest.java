package com.nyangtodac.auth.controller.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String codeVerifier;
    private String authorizationCode;
    private String redirectUri;
}
