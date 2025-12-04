package com.nyangtodac.external.ai.retry;

import com.nyangtodac.external.ai.infrastructure.OpenAiException;

public class OpenAiRetryableException extends OpenAiException {
    public OpenAiRetryableException(String message) {
        super(message);
    }

    public OpenAiRetryableException(Throwable cause) {
        super(cause);
    }

    public OpenAiRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
