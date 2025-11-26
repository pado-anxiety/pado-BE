package com.nyangtodac.chat.application;

import lombok.Getter;

import java.util.List;
@Getter
public class MessageContext {

    private final List<Message> messages;

    public MessageContext(List<Message> messages) {
        this.messages = messages;
    }

}

