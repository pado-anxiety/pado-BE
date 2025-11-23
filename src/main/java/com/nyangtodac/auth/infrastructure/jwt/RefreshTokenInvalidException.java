package com.nyangtodac.auth.infrastructure.jwt;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException(Throwable cause) {
        super(cause);
    }
}
