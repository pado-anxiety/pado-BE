package com.pado.external.rabbitmq;

import com.pado.chat.domain.Chatting;
import lombok.Getter;

@Getter
public class ChattingPersistMessage {

    private Long userId;
    private Chatting chatting;

    public ChattingPersistMessage(Long userId, Chatting chatting) {
        this.userId = userId;
        this.chatting = chatting;
    }

    public ChattingPersistMessage() {
    }
}
