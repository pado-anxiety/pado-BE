package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import com.nyangtodac.chat.infrastructure.MessageDbRepository;
import com.nyangtodac.chat.infrastructure.MessageRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private static final int CONTEXT_SIZE = 10;
    private static final int CHAT_HISTORY_SIZE = 30;

    private final MessageRedisRepository messageRedisRepository;
    private final MessageDbRepository messageDbRepository;

    public void saveMessages(Long userId, List<Message> messages) {
        List<Message> overflowMessages = messageRedisRepository.saveMessages(userId, messages);
        if (!overflowMessages.isEmpty()) {
            messageDbRepository.asyncBatch(userId, overflowMessages).exceptionally(
                    e -> {
                        log.error("Failed to flush [overflow messages] for userId: {}", userId, e);
                        messageRedisRepository.saveFlushFailedMessages(userId, messages);
                        return null;
                    }
            );
        }
    }

    public ChatMessagesResponse getRecentMessages(Long userId) {
        List<Message> messages = new ArrayList<>(messageRedisRepository.findRecentMessages(userId, CHAT_HISTORY_SIZE));
        Collections.reverse(messages);
        if (messages.size() < CHAT_HISTORY_SIZE) {
            int left = CHAT_HISTORY_SIZE - messages.size();
            List<Message> dbMessages = messageDbRepository.findTopNByUserIdOrderByTsidDesc(userId, left);
            messages.addAll(dbMessages);
        }
        return new ChatMessagesResponse(messages);
    }

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
