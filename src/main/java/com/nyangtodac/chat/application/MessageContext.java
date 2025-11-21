package com.nyangtodac.chat.application;

import lombok.Getter;

import java.util.List;
@Getter
public class MessageContext {

    private final List<Message> messages;

    public MessageContext(List<Message> messages) {
        this.messages = messages;
    }

    @Getter
    public static class Message {

        private final String content;
        private final String role;

        public Message(String content, String role) {
            this.content = content;
            this.role = role;
        }

    }
}

