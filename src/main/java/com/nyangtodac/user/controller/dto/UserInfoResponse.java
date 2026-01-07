package com.nyangtodac.user.controller.dto;

import lombok.Getter;

@Getter
public class UserInfoResponse {

    private final String email;
    private final String name;

    public UserInfoResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
