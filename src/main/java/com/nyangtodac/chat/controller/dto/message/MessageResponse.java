package com.nyangtodac.chat.controller.dto.message;

import com.nyangtodac.chat.domain.Type;
import com.nyangtodac.chat.controller.dto.ChattingResponse;
import com.nyangtodac.chat.controller.dto.Sender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponse extends ChattingResponse {
    private final Sender sender;
    private final String message;

    public MessageResponse(Type type, Sender sender, String message, LocalDateTime time) {
        super(type, time);
        this.sender = sender;
        this.message = message;
    }
}
