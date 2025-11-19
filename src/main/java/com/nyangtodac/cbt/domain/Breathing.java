package com.nyangtodac.cbt.domain;

import lombok.Getter;

@Getter
public class Breathing implements CBT {

    private final int durationSeconds;

    public Breathing(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
