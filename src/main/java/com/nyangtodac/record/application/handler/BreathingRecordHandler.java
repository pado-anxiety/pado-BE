package com.nyangtodac.record.application.handler;

import com.nyangtodac.record.dto.command.BreathingRecordCommand;
import com.nyangtodac.record.dto.command.CBTRecordCommand;
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
