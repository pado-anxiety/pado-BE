package com.nyangtodac.user.domain;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final LoginType loginType;

    private String name;

    public User(Long id, String email, LoginType loginType, String name) {
        this.id = id;
        this.email = email;
        this.loginType = loginType;
        this.name = name;
    }
}
