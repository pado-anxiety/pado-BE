package com.pado.auth.infrastructure.jwt;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException(Throwable cause) {
        super(cause);
    }
}
