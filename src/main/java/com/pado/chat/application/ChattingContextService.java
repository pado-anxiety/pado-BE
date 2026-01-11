package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.chat.infrastructure.ChattingDBRepository;
import com.pado.chat.infrastructure.RecentChattingRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ChattingContextService {

    private final RecentChattingRedisRepository recentChattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    private final int contextSize;

    public ChattingContextService(
            RecentChattingRedisRepository recentChattingRedisRepository,
            ChattingDBRepository chattingDBRepository,
            @Value("${chat.context.size}") int contextSize) {
        this.recentChattingRedisRepository = recentChattingRedisRepository;
        this.chattingDBRepository = chattingDBRepository;
        this.contextSize = contextSize;
    }

    @Transactional(readOnly = true)
    public ChattingContext makeContext(Long userId, Chatting userChatting) {
        List<Chatting> recentChattings = recentChattingRedisRepository.getRecentChattings(userId);
        if (recentChattings.isEmpty() || recentChattings.size() < contextSize) {
            //cache miss
            int left = contextSize - recentChattings.size();
            List<Chatting> chattings = chattingDBRepository.findRecentMessages(userId, left);
            recentChattings.addAll(chattings);
            recentChattings.sort(Comparator.comparing(Chatting::getTsid));
        }
        recentChattings.add(userChatting);
        return new ChattingContext(recentChattings);
    }

    public void appendContext(Long userId, List<Chatting> chattings) {
        recentChattingRedisRepository.appendContextCache(userId, chattings);
    }

}
