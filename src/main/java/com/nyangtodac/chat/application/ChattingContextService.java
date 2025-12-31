package com.nyangtodac.chat.application;

import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.ChattingContext;
import com.nyangtodac.chat.infrastructure.ChattingContextRedisRepository;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingContextService {

    private final ChattingContextRedisRepository chattingContextRepository;
    private final ChattingDBRepository chattingDBRepository;

    @Value("${chat.context.size}") private int contextSize;

    @Transactional(readOnly = true)
    public ChattingContext makeContext(Long userId, Chatting userChatting) {
        List<Chatting> recentChattings = chattingContextRepository.getContext(userId);
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
        chattingContextRepository.appendContextCache(userId, chattings);
    }

}
