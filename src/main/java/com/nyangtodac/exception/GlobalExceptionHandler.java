package com.nyangtodac.exception;

import com.nyangtodac.external.ai.OpenAiException;
import com.nyangtodac.external.ai.retry.OpenAiNonRetryableException;
import com.nyangtodac.external.ai.retry.OpenAiRetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OpenAiException.class)
    public ResponseEntity<ErrorResponse> handleOpenAiException(OpenAiException e) {
        if (e instanceof OpenAiNonRetryableException) {
            log.error("OpenAiNonRetryableException occurred", e);
        } else if (e instanceof OpenAiRetryableException) {
            log.error("OpenAiRetryableException occurred", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("AI 기능이 일시적으로 원활하지 않습니다. 잠시 후 다시 시도해 주세요."));
    }
}
