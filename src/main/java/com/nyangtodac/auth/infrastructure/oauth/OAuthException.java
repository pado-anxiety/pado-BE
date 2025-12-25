package com.nyangtodac.auth.infrastructure.oauth;

import lombok.Getter;

@Getter
public class OAuthException extends RuntimeException {

    private final String bodyErrorMessage;

    public OAuthException(String message, String bodyErrorMessage) {
        super(message);
        this.bodyErrorMessage = bodyErrorMessage;
    }

}
