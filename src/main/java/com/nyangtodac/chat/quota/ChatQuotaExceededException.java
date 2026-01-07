package com.nyangtodac.chat.quota;

import lombok.Getter;

import java.time.Duration;

@Getter
public class ChatQuotaExceededException extends RuntimeException {
    private final QuotaStatus quotaStatus;

    public ChatQuotaExceededException(QuotaStatus quotaStatus) {
        this.quotaStatus = quotaStatus;
    }

    public String getQuotaExceededMessage() {
        Duration timeToRefill = quotaStatus.getTimeToRefill();
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
