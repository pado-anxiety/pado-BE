package com.nyangtodac.chat.application;

import lombok.Getter;

@Getter
public class Message {
    private final String content;
    private final String role;

    public Message(String content, String role) {
        this.content = content;
        this.role = role;
    }
}
