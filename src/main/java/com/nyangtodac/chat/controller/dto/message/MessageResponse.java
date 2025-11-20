package com.nyangtodac.chat.controller.dto.message;

import lombok.Getter;

import java.util.List;

@Getter
public class MessageResponse {

    private final List<String> replies;

    public MessageResponse(List<String> replies) {
        this.replies = replies;
    }
}
