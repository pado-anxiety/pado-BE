package com.pado.user.domain;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private String email;
    private String sub;
    private final LoginType loginType;

    private String name;
    private String refreshToken;

    public User(Long id, String email, String sub, LoginType loginType, String name, String refreshToken) {
        this.id = id;
        this.email = email;
        this.sub = sub;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
    }

    public User(String email, String sub, String name, LoginType loginType) {
        this(null, email, sub, loginType, name, null);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
