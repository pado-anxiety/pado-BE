package com.nyangtodac.external.ai;

public class OpenAiException extends RuntimeException {

    public OpenAiException(String message) {
        super(message);
    }

    public OpenAiException(Throwable cause) {
        super(cause);
    }
}
