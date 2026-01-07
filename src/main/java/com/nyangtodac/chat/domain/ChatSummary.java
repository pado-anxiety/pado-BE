package com.nyangtodac.chat.domain;

import lombok.Getter;

@Getter
public class ChatSummary {

    private final Long fromTsid;
    private final Long toTsid;
    private final String summaryText;

    public ChatSummary(Long fromTsid, Long toTsid, String summaryText) {
        this.fromTsid = fromTsid;
        this.toTsid = toTsid;
        this.summaryText = summaryText;
    }
}
