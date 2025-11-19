package com.nyangtodac.record.application;

import com.nyangtodac.cbt.domain.CBT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CBTRecordService {

    public void record(Long userId, CBT cbt) {

    }
}
