package com.pado.auth.infrastructure.jwt;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(Throwable cause) {
        super(cause);
    }
}
