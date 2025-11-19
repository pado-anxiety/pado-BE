package com.nyangtodac.record.application;

import org.springframework.stereotype.Component;

@Component
public class BreathingRecordHandler implements RecordHandler {

    @Override
    public boolean supports(CBTRecordCommand command) {
        return command instanceof BreathingRecordCommand;
    }

    @Override
    public void record(Long userId, CBTRecordCommand command) {
        BreathingRecordCommand breathingCommand = (BreathingRecordCommand) command;
    }
}
