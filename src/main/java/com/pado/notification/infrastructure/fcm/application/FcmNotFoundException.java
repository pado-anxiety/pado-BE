package com.pado.notification.infrastructure.fcm.application;

import lombok.Getter;

@Getter
public class FcmNotFoundException extends RuntimeException {
    private final Long userId;

    public FcmNotFoundException(Long userId) {
        this.userId = userId;
    }
}
