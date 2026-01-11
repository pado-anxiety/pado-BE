package com.pado.chat.domain;

import lombok.Getter;

import java.util.List;
@Getter
public class ChattingContext {

    private final List<Chatting> chattings;

    public ChattingContext(List<Chatting> chattings) {
        this.chattings = chattings;
    }

}

