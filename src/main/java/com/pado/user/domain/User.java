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

    private Boolean marketingConsent;
    private Instant marketingConsentAt;
    private Instant marketingRevokedAt;

    public User(Long id, String email, String sub, LoginType loginType, String name, String refreshToken, String oauthRefreshToken, Instant lastLoginTime, String timezone, Boolean marketingConsent, Instant marketingConsentAt, Instant marketingRevokedAt) {
        this.id = id;
        this.email = email;
        this.sub = sub;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
        this.oAuthRefreshToken = oauthRefreshToken;
        this.lastLoginTime = lastLoginTime;
        this.timezone = timezone;
        this.marketingConsent = marketingConsent;
        this.marketingConsentAt = marketingConsentAt;
        this.marketingRevokedAt = marketingRevokedAt;
    }

    public User(String email, String sub, String name, LoginType loginType, String oAuthRefreshToken, String timezone) {
        this(null, email, sub, loginType, name, null, oAuthRefreshToken, Instant.now(), timezone, null, null, null);
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

    public void updateMarketingConsent(boolean agreed) {
        if (agreed) {
            agreeMarketing();
        } else {
            revokeMarketing();
        }
    }

    private void agreeMarketing() {
        if (marketingConsent) return;
        marketingConsent = true;
        marketingConsentAt = Instant.now();
        marketingRevokedAt = null;
    }

    private void revokeMarketing() {
        if (!marketingConsent) return;
        marketingConsent = false;
        marketingRevokedAt = Instant.now();
    }
}
