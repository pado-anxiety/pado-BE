package com.nyangtodac.auth.infrastructure.jwt;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(Throwable cause) {
        super(cause);
    }
}
