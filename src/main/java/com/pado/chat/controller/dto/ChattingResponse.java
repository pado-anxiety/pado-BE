package com.pado.chat.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingResponse {
    private final Sender sender;
    private final String message;
    private final LocalDateTime time;

    public ChattingResponse(Sender sender, String message, LocalDateTime localDateTime) {
        this.sender = sender;
        this.message = message;
        this.time = localDateTime;
    }
}
