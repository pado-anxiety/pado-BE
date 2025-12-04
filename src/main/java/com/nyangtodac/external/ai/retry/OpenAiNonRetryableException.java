package com.nyangtodac.external.ai.retry;

import com.nyangtodac.external.ai.infrastructure.OpenAiException;

public class OpenAiNonRetryableException extends OpenAiException {
    public OpenAiNonRetryableException(String message) {
        super(message);
    }

    public OpenAiNonRetryableException(Throwable cause) {
        super(cause);
    }

    public OpenAiNonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
