package com.nyangtodac.chat.application;

import com.nyangtodac.chat.infrastructure.MessageDbRepository;
import com.nyangtodac.external.redis.MessageRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final int CONTEXT_SIZE = 10;

    private final MessageRedisRepository messageRedisRepository;
    private final MessageDbRepository messageDbRepository;

    public void saveMessages(Long userId, List<Message> messages) {
        List<Message> overflowMessages = messageRedisRepository.saveMessages(userId, messages);
        if (!overflowMessages.isEmpty()) {
            messageDbRepository.asyncFlush(userId, overflowMessages);
        }
    }

    public List<Message> makeContext(Long userId) {
        List<Message> messages = messageRedisRepository.findRecentMessages(userId, CONTEXT_SIZE);
        int left = CONTEXT_SIZE - messages.size();

        if (left > 0) {
            List<Message> dbMessages = messageDbRepository.findTopNByUserIdOrderByCreatedAtDescIdDesc(userId, left);
            Collections.reverse(dbMessages);
            messages.addAll(0, dbMessages);
        }

        return messages;
    }
}
