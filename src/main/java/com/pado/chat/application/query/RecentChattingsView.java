package com.pado.chat.application.query;

import lombok.Getter;

import java.util.List;

@Getter
public class RecentChattingsView {

    private final List<ChattingItemView> chattings;
    private final Long cursor;

    public RecentChattingsView(List<ChattingItemView> chattings, Long cursor) {
        this.chattings = chattings;
        this.cursor = cursor;
    }
}
