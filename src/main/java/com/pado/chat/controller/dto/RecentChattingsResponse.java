package com.pado.chat.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RecentChattingsResponse {
    private final List<ChattingResponse> content;
    private final Long cursor;

    public RecentChattingsResponse(List<ChattingResponse> chattings, Long cursor) {
        this.content = chattings;
        this.cursor = cursor;
    }

}
