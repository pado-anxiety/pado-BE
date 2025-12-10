package com.nyangtodac.test;

import lombok.Getter;

@Getter
public class DevLogin {
    private final String token;

    public DevLogin(String token) {
        this.token = token;
    }
}
