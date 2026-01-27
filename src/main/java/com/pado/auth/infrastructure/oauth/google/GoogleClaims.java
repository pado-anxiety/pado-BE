package com.pado.auth.infrastructure.oauth.google;

import lombok.Getter;

@Getter
public class GoogleClaims {

    private final String sub;
    private final String email;
    private final String name;

    public GoogleClaims(String sub, String email, String name) {
        this.sub = sub;
        this.email = email;
        this.name = name;
    }
}
