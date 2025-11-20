package com.nyangtodac.chat.application;

import lombok.Getter;

import java.util.List;
@Getter
public class RecentMessages {
    private final List<String> messages;

    public RecentMessages(List<String> messages) {
        this.messages = messages;
    }

    public String asString() {
        return String.join("\n", messages);
    }
}
