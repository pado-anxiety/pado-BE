package com.nyangtodac.chat.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class RecentChattings {
    private final List<Chatting> chattings;
    private final Long cursor;

    public RecentChattings(List<Chatting> chattings, Long cursor) {
        this.chattings = chattings;
        this.cursor = cursor;
    }
}
