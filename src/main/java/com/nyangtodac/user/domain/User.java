package com.nyangtodac.user.domain;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final LoginType loginType;

    private String name;
    private String refreshToken;

    public User(Long id, String email, LoginType loginType, String name, String refreshToken) {
        this.id = id;
        this.email = email;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
    }

    public User(String email, LoginType loginType) {
        this(null, email, loginType, null, null);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
