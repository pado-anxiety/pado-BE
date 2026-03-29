package com.pado.chat.mq;

import com.pado.chat.domain.Chatting;
import com.pado.chat.infrastructure.ChattingDBRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingFallbackService {

    private final ChattingDBRepository chattingRepository;

    @Transactional
    public void fallback(Long userId, List<Chatting> chattings) {
        try {
            chattingRepository.saveAll(userId, chattings);
        } catch (Exception e) {
            log.error("Fallback failed. userId={}", userId);
        }
    }
}
