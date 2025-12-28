package com.nyangtodac.chat.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatSummaries {

    private final List<ChatSummary> summaryList;

    public ChatSummaries(List<ChatSummary> summaryList) {
        this.summaryList = summaryList;
    }
}
