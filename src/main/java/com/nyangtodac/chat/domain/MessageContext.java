package com.nyangtodac.chat.domain;

import lombok.Getter;

import java.util.List;
@Getter
public class MessageContext {

    private final List<Chatting> chattings;

    public MessageContext(List<Chatting> chattings) {
        this.chattings = chattings;
    }

}

