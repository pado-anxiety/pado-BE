package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.application.Type;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class ChattingResponse {
    private final Type type;
    private final LocalDateTime time;

    public ChattingResponse(Type type, LocalDateTime time) {
        this.type = type;
        this.time = time;
    }
}
