package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.chat.infrastructure.ChattingDBRepository;
import com.pado.chat.infrastructure.RecentChattingRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChattingCacheWarmupService {

    private final RecentChattingRedisRepository chattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    private final int contextSize;

    public ChattingCacheWarmupService(
            RecentChattingRedisRepository chattingRedisRepository,
            ChattingDBRepository chattingDBRepository,
            @Value("${chat.context.size}") int contextSize
    ) {
        this.chattingRedisRepository = chattingRedisRepository;
        this.contextSize = contextSize;
        this.chattingDBRepository = chattingDBRepository;
    }

    public void warmUp(Long userId) {
        List<Chatting> cached = chattingRedisRepository.getRecentChattings(userId);
        if (cached.size() >= contextSize) return;
        long cursor;
        if (cached.isEmpty()) {
            cursor = Long.MAX_VALUE;
        } else {
            cursor = cached.get(cached.size() - 1).getTsid();
        }
        List<Chatting> chattings = chattingDBRepository.findRecentChattingsLessThanCursor(userId, cursor, contextSize - cached.size());
        chattingRedisRepository.appendContextCache(userId, chattings);
    }
}
