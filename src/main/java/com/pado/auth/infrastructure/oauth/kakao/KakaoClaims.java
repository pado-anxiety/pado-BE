package com.pado.auth.infrastructure.oauth.kakao;

import lombok.Getter;

@Getter
public class KakaoClaims {
    private final String sub;
    private final String email;
    private final String name;

    public KakaoClaims(String sub, String email, String name) {
        this.sub = sub;
        this.email = email;
        this.name = name;
    }
}
