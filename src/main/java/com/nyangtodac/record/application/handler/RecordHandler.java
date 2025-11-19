package com.nyangtodac.record.application.handler;

import com.nyangtodac.record.dto.command.CBTRecordCommand;

public interface RecordHandler {
    boolean supports(CBTRecordCommand command);
    void record(Long userId, CBTRecordCommand command);
}
