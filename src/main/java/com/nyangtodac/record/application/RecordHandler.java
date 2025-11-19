package com.nyangtodac.record.application;

public interface RecordHandler {
    boolean supports(CBTRecordCommand command);
    void record(Long userId, CBTRecordCommand command);
}
