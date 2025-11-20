package com.nyangtodac.chat.controller.dto;

import lombok.Getter;

@Getter
public enum Sender {
    USER("user"),
    AI("assistant");

    private final String role;

    Sender(String role) {
        this.role = role;
    }
}
