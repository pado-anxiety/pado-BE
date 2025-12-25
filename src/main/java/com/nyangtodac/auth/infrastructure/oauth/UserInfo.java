package com.nyangtodac.auth.infrastructure.oauth;

import lombok.Getter;

@Getter
public class UserInfo {

    private final String email;
    private final String name;

    public UserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
