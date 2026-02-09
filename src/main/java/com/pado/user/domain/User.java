package com.pado.user.domain;

import lombok.Getter;

import java.time.Instant;

@Getter
public class User {
    private final Long id;
    private String email;
    private final String sub;
    private final LoginType loginType;

    private String name;
    private String refreshToken;

    private String oAuthRefreshToken;

    private Instant lastLoginTime;
    private String timezone;

    public User(Long id, String email, String sub, LoginType loginType, String name, String refreshToken, String oauthRefreshToken, Instant lastLoginTime, String timezone) {
        this.id = id;
        this.email = email;
        this.sub = sub;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
        this.oAuthRefreshToken = oauthRefreshToken;
        this.lastLoginTime = lastLoginTime;
        this.timezone = timezone;
    }

    public User(String email, String sub, String name, LoginType loginType, String oAuthRefreshToken, String timezone) {
        this(null, email, sub, loginType, name, null, oAuthRefreshToken, Instant.now(), timezone);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateOAuthRefreshToken(String oAuthRefreshToken) {
        this.oAuthRefreshToken = oAuthRefreshToken;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateLastLoginTime() {
        this.lastLoginTime = Instant.now();
    }

    public void updateTimezone(String timezone) {
        this.timezone = timezone;
    }
}
