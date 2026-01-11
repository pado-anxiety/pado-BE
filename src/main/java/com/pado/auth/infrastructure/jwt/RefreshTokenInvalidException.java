package com.pado.auth.infrastructure.jwt;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException(Throwable cause) {
        super(cause);
    }
}
