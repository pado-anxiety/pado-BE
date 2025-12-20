package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.controller.dto.mapper.ChattingResponseMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatMessagesResponse {

    private final List<ChattingResponse> content;

    public ChatMessagesResponse(List<Chatting> content) {
        this.content = content.stream().map(ChattingResponseMapper::from).toList();
    }

}
