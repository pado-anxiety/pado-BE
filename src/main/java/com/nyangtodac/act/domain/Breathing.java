package com.nyangtodac.act.domain;

import lombok.Getter;

@Getter
public class Breathing {

    private final int durationSeconds;

    public Breathing(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
