package com.pado.chat.mq;

import com.pado.chat.domain.Chatting;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ChattingPersistMessage {

    private Long userId;
    private List<Chatting> chattings;

    public ChattingPersistMessage(Long userId, List<Chatting> chattings) {
        this.userId = userId;
        this.chattings = chattings;
    }

    public ChattingPersistMessage() {
    }
}
