package com.pado.external.ai.infrastructure;

public abstract class OpenAiException extends RuntimeException {

    public OpenAiException(String message) {
        super(message);
    }

    public OpenAiException(Throwable cause) {
        super(cause);
    }

    public OpenAiException(String message, Throwable cause) {
        super(message, cause);
    }
}
