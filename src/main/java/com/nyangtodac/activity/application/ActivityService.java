package com.nyangtodac.activity.application;

import com.nyangtodac.cbt.domain.CBT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ActivityService {

    public void record(Long userId, CBT cbt) {

    }
}
