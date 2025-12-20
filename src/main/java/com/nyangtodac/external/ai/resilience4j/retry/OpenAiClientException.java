package com.nyangtodac.external.ai.resilience4j.retry;

import com.nyangtodac.external.ai.infrastructure.OpenAiException;

public class OpenAiClientException extends OpenAiException {
    public OpenAiClientException(String message) {
        super(message);
    }

    public OpenAiClientException(Throwable cause) {
        super(cause);
    }

    public OpenAiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
