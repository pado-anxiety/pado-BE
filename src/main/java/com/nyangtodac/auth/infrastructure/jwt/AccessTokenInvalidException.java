package com.nyangtodac.auth.infrastructure.jwt;

public class AccessTokenInvalidException extends RuntimeException {
    public AccessTokenInvalidException(Throwable cause) {
        super(cause);
    }

    public AccessTokenInvalidException(String message) {
        super(message);
    }
}
