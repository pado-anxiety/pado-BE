package com.pado.user.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {
    private final Long id;
    private String email;
    private final String sub;
    private final LoginType loginType;

    private String name;
    private String refreshToken;

    private String oAuthRefreshToken;

    private LocalDateTime lastLoginTime;
    private String timezone;

    public User(Long id, String email, String sub, LoginType loginType, String name, String refreshToken, String oauthRefreshToken, LocalDateTime lastLoginTime, String timezone) {
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

    public User(String email, String sub, String name, LoginType loginType, String oAuthRefreshToken) {
        this(null, email, sub, loginType, name, null, oAuthRefreshToken, LocalDateTime.now(), null);
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
        this.lastLoginTime = LocalDateTime.now();
    }

    public void updateTimezone(String timezone) {
        this.timezone = timezone;
    }
}
