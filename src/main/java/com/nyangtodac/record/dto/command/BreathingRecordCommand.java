package com.nyangtodac.record.dto.command;

import lombok.Getter;

@Getter
public class BreathingRecordCommand implements CBTRecordCommand {

    private final int durationSeconds;

    public BreathingRecordCommand(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
