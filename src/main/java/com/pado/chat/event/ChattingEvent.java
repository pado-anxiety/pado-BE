package com.pado.chat.event;

import com.pado.chat.domain.Chatting;
import lombok.Getter;

@Getter
public class ChattingEvent {
    private final Long userId;
    private final Chatting userChatting;
    private final Chatting reply;

    public ChattingEvent(Long userId, Chatting userChatting, Chatting reply) {
        this.userId = userId;
        this.userChatting = userChatting;
        this.reply = reply;
    }
}
