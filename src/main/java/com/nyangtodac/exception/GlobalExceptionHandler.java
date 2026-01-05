package com.nyangtodac.exception;

import com.nyangtodac.act.application.ACTAccessDeniedException;
import com.nyangtodac.act.application.ACTRecordNotFoundException;
import com.nyangtodac.act.application.InvalidActRecordRequestException;
import com.nyangtodac.act.recommend.ACTRecommendQuotaExceededException;
import com.nyangtodac.auth.infrastructure.oauth.OAuthException;
import com.nyangtodac.chat.quota.ChatQuotaExceededException;
import com.nyangtodac.external.ai.infrastructure.OpenAiException;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiClientException;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiServerException;
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
        if (e instanceof OpenAiClientException) {
            log.error("OpenAiClientException occurred", e);
        } else if (e instanceof OpenAiServerException) {
            log.error("OpenAiServerException occurred", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("AI 기능이 일시적으로 원활하지 않습니다. 잠시 후 다시 시도해 주세요."));
    }

    @ExceptionHandler(ChatQuotaExceededException.class)
    public ResponseEntity<ErrorResponse> handleChatQuotaExceededException(ChatQuotaExceededException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getQuotaExceededMessage()));
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<ErrorResponse> handleOAuthException(OAuthException e) {
        log.error("OAuthException occurred. Error Response from OAuth2 Server: {}", e.getBodyErrorMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("소셜 로그인에 실패했습니다. 다시 시도해주세요."));
    }

    @ExceptionHandler(ACTRecommendQuotaExceededException.class)
    public ResponseEntity<ErrorResponse> handleACTRecommendQuotaExceededException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(InvalidActRecordRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidActRecordRequestException(InvalidActRecordRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ACTRecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleACTRecordNotFoundException(ACTRecordNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("ACT 기록을 찾을 수 없습니다. recordId: " + e.getRecordId()));
    }

    @ExceptionHandler(ACTAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleACTAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("ACT 조회 권한이 없습니다."));
    }
}
