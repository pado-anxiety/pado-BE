package com.nyangtodac.exception;

import com.nyangtodac.chat.quota.ChatQuotaExceededException;
import com.nyangtodac.chat.quota.QuotaStatus;
import com.nyangtodac.external.ai.infrastructure.OpenAiException;
import com.nyangtodac.external.ai.retry.OpenAiClientException;
import com.nyangtodac.external.ai.retry.OpenAiServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Duration;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OpenAiException.class)
    public ResponseEntity<ErrorResponse> handleOpenAiException(OpenAiException e) {
        if (e instanceof OpenAiClientException) {
            log.error("OpenAiNonRetryableException occurred", e);
        } else if (e instanceof OpenAiServerException) {
            log.error("OpenAiRetryableException occurred", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("AI 기능이 일시적으로 원활하지 않습니다. 잠시 후 다시 시도해 주세요."));
    }

    @ExceptionHandler(ChatQuotaExceededException.class)
    public ResponseEntity<ErrorResponse> handleChatQuotaExceededException(ChatQuotaExceededException e) {
        QuotaStatus quotaStatus = e.getQuotaStatus();
        String formatted = formatQuotaMessage(quotaStatus.getTimeToRefill());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(formatted));
    }

    private String formatQuotaMessage(Duration timeToRefill) {
        long hours = timeToRefill.toHours();
        long minutes = timeToRefill.minusHours(hours).toMinutes();
        long seconds = timeToRefill.minusHours(hours).minusMinutes(minutes).getSeconds();

        StringBuilder time = new StringBuilder("입력 가능 시간이 ");
        if (hours > 0) {
            time.append(String.format("%d시간 %d분 %d초", hours, minutes, seconds));
        } else if (minutes > 0) {
            time.append(String.format("%d분 %d초", minutes, seconds));
        } else {
            time.append(String.format("%d초", seconds));
        }
        time.append(" 남았습니다.");
        return time.toString();
    }
}
