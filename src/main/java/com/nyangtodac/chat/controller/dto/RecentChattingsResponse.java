package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.application.Chatting;
import com.nyangtodac.chat.controller.dto.mapper.ChattingResponseMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class RecentChattingsResponse {
    private final List<ChattingResponse> content;
    private final Long cursor;

    public RecentChattingsResponse(List<Chatting> content, Long cursor) {
        this.content = content.stream().map(ChattingResponseMapper::from).toList();
        this.cursor = cursor;
    }
}
