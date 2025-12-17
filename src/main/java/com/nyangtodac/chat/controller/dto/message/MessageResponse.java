package com.nyangtodac.chat.controller.dto.message;

import com.nyangtodac.chat.application.Type;
import com.nyangtodac.chat.controller.dto.Sender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponse {
    private final Type type;
    private final Sender sender;
    private final String message;
    private final LocalDateTime time;

    public MessageResponse(Type type, Sender sender, String message, LocalDateTime time) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
}
