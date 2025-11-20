package com.nyangtodac.chat.application;

import lombok.Getter;

import java.util.List;
@Getter
public class RecentMessages {
    private final List<Message> messages;

    public RecentMessages(List<Message> messages) {
        this.messages = messages;
    }

}

