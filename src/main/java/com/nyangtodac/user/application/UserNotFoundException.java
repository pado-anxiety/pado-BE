package com.nyangtodac.user.application;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final Long userId;

    public UserNotFoundException(Long userId) {
        this.userId = userId;
    }
}
