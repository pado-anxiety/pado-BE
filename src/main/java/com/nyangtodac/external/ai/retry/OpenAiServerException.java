package com.nyangtodac.external.ai.retry;

import com.nyangtodac.external.ai.infrastructure.OpenAiException;

public class OpenAiServerException extends OpenAiException {
    public OpenAiServerException(String message) {
        super(message);
    }

    public OpenAiServerException(Throwable cause) {
        super(cause);
    }

    public OpenAiServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
