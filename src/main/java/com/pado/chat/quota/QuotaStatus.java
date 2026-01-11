package com.pado.chat.quota;

import lombok.Getter;

import java.time.Duration;

@Getter
public class QuotaStatus {

    private final int quota;
    private final Duration timeToRefill;

    public QuotaStatus(long nanoTimesToRefill, long quota) {
        this.timeToRefill = Duration.ofNanos(nanoTimesToRefill);
        this.quota = (int) quota;
    }
}
