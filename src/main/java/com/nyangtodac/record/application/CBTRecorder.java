package com.nyangtodac.record.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CBTRecorder {

    private final List<RecordHandler> handlers;

    public void record(Long userId, CBTRecordCommand command) {
        RecordHandler handler = handlers.stream()
                .filter(c -> c.supports(command))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unsupported CBT"));
        handler.record(userId, command);
    }
}
