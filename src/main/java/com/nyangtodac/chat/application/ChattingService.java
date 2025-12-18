package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import com.nyangtodac.chat.infrastructure.ChattingRedisRepository;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {

    private static final int CONTEXT_SIZE = 10;
    private static final int CHAT_HISTORY_SIZE = 30;

    private final ChattingRedisRepository chattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    public void saveChattings(Long userId, List<Chatting> chattings) {
        List<Chatting> overflowChattings = chattingRedisRepository.saveMessages(userId, chattings);
        if (!overflowChattings.isEmpty()) {
            chattingDBRepository.asyncBatch(userId, overflowChattings).exceptionally(
                    e -> {
                        log.error("Failed to flush [overflow messages] for userId: {}", userId, e);
                        chattingRedisRepository.saveFlushFailedMessages(userId, chattings);
                        return null;
                    }
            );
        }
    }

    @Transactional(readOnly = true)
    public ChatMessagesResponse getRecentChattings(Long userId) {
        List<Chatting> chattings = new ArrayList<>(chattingRedisRepository.findRecentChattings(userId, CHAT_HISTORY_SIZE));
        Collections.reverse(chattings);
        if (chattings.size() < CHAT_HISTORY_SIZE) {
            int left = CHAT_HISTORY_SIZE - chattings.size();
            List<Chatting> dbMessages = chattingDBRepository.findRecentChattings(userId, left);
            chattings.addAll(dbMessages);
        }
        return new ChatMessagesResponse(chattings);
    }

    @Transactional(readOnly = true)
    public List<Message> makeContext(Long userId) {
        List<Message> messages = new ArrayList<>(chattingRedisRepository.findRecentMessages(userId, CONTEXT_SIZE));
        int left = CONTEXT_SIZE - messages.size();

        if (left > 0) {
            List<Message> dbMessages = chattingDBRepository.findRecentMessages(userId, left);
            Collections.reverse(dbMessages);
            messages.addAll(0, dbMessages);
        }

        return messages;
    }
}
