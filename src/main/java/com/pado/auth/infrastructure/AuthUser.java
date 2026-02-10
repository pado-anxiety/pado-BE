package com.pado.auth.infrastructure;

import lombok.Getter;

import java.time.ZoneId;

@Getter
public class AuthUser {
    private final Long userId;
    private final ZoneId zoneId;

    public AuthUser(Long userId, String timezone) {
        this.userId = userId;
        this.zoneId = ZoneId.of(timezone);
    }
}
