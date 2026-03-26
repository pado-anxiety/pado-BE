package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.chat.infrastructure.ChattingDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingPersistService {

    private final ChattingDBRepository chattingRepository;

    @Transactional
    public void saveAll(Long userId, List<Chatting> chattings) {
        chattingRepository.saveAll(userId, chattings);
    }
}
