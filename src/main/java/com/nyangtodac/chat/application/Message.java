package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Message {

    private String content;
    private String role;
    private LocalDateTime createdAt;

    public Message(String content, String role) {
        this.content = content;
        this.role = role.toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    public Message(String content, Sender sender, LocalDateTime localDateTime) {
        this.content = content;
        this.role = sender.getRole();
        this.createdAt = localDateTime;
    }

    public Message() {
    }
}