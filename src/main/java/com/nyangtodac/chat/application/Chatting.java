package com.nyangtodac.chat.application;

import lombok.Getter;

@Getter
public abstract class Chatting {
    private final Type type;
    private final Long tsid;

    public Chatting(Type type, Long tsid) {
        this.type = type;
        this.tsid = tsid;
    }
}
