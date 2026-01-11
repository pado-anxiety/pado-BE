package com.pado.auth.controller.dto;

import lombok.Getter;

@Getter
public class TokenReissueRequest {

    private final String refreshToken;

    public TokenReissueRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
