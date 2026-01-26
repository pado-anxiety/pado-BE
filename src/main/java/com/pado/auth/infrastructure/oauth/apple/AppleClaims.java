package com.pado.auth.infrastructure.oauth.apple;

import lombok.Getter;

@Getter
public class AppleClaims {

    private String sub;
    private String email;

    public AppleClaims(String sub, String email) {
        this.sub = sub;
        this.email = email;
    }
}
