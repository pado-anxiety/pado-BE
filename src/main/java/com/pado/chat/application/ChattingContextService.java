package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.chat.infrastructure.ChattingDBRepository;
import com.pado.chat.infrastructure.RecentChattingRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChattingContextService {

    private final RecentChattingRedisRepository recentChattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;
    private final ChattingEncryptService encryptService;

    private final int contextSize;

    public ChattingContextService(
            RecentChattingRedisRepository recentChattingRedisRepository,
            ChattingDBRepository chattingDBRepository,
            ChattingEncryptService encryptService,
            @Value("${chat.context.size}") int contextSize) {
        this.recentChattingRedisRepository = recentChattingRedisRepository;
        this.chattingDBRepository = chattingDBRepository;
        this.contextSize = contextSize;
        this.encryptService = encryptService;
    }

    @Transactional(readOnly = true)
    public ChattingContext makeContext(Long userId, Chatting userChatting) {
        List<Chatting> recentChattings = recentChattingRedisRepository.getRecentChattings(userId);
        if (recentChattings.isEmpty() || recentChattings.size() < contextSize) {
            //cache miss
            int left = contextSize - recentChattings.size();
            long cursor;
            if (recentChattings.isEmpty()) {
                cursor = Long.MAX_VALUE;
            } else {
                cursor = recentChattings.get(recentChattings.size() - 1).getTsid();
            }
            List<Chatting> chattings = chattingDBRepository.findRecentChattingsLessThanCursor(userId, cursor, left);
            recentChattings.addAll(chattings);
            recentChattings.sort(Comparator.comparing(Chatting::getTsid));
        }
        List<Chatting> decrypted = new ArrayList<>(recentChattings.stream().map(encryptService::decrypt).toList());
        decrypted.add(userChatting);
        return new ChattingContext(decrypted);
    }

    public void appendContext(Long userId, List<Chatting> encrypted) {
        recentChattingRedisRepository.appendContextCache(userId, encrypted);
    }

}
