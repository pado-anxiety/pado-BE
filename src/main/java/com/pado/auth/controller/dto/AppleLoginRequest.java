package com.pado.auth.controller.dto;

import lombok.Getter;

@Getter
public class AppleLoginRequest {
    private String authorizationCode;
    private String fullName;
}
