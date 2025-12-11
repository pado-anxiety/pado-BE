package com.nyangtodac.chat.quota;

import lombok.Getter;

@Getter
public class ChatQuotaExceededException extends RuntimeException {
    private final QuotaStatus quotaStatus;

    public ChatQuotaExceededException(QuotaStatus quotaStatus) {
        this.quotaStatus = quotaStatus;
    }
}
