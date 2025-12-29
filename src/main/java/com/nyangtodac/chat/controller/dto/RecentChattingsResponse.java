package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.domain.Chatting;
import lombok.Getter;

import java.util.List;

@Getter
public class RecentChattingsResponse {
    private final List<ChattingResponse> content;
    private final Long cursor;

    public RecentChattingsResponse(List<Chatting> chattings, Long cursor) {
        this.content = chattings.stream().map(c -> new ChattingResponse(Sender.valueOf(c.getSender()), c.getMessage(), c.getTsid())).toList();
        this.cursor = cursor;
    }

}
