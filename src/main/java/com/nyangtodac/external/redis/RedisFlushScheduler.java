package com.nyangtodac.external.redis;

import com.nyangtodac.chat.application.Message;
import com.nyangtodac.chat.infrastructure.MessageDbRepository;
import com.nyangtodac.chat.infrastructure.MessageRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisFlushScheduler {

    private final MessageRedisRepository messageRedisRepository;
    private final MessageDbRepository messageDbRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void flushRemainingMessages() {
        List<Long> activeUserIds = messageRedisRepository.getActiveUserIds();

        for (Long userId : activeUserIds) {
            List<Message> messages = messageRedisRepository.flushAllMessagesByUserId(userId);
            if (!messages.isEmpty()) {
                messageDbRepository.asyncBatch(userId, messages).exceptionally(
                        e -> {
                            log.error("Failed to flush [remaining messages] for userId: {}", userId, e);
                            messageRedisRepository.saveFlushFailedMessages(userId, messages);
                            return null;
                        }
                );
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void retryFailedFlushMessages() {
        List<Long> flushFailedUserIds = messageRedisRepository.getFlushFailedUserIds();

        for (Long userId : flushFailedUserIds) {
            List<Message> messages = messageRedisRepository.flushFailedMessagesByUserId(userId);
            if (!messages.isEmpty()) {
                messageDbRepository.asyncBatch(userId, messages).exceptionally(
                        e -> {
                            log.error("Failed to flush [flush failed messages] for userId: {}", userId, e);
                            messageRedisRepository.saveFlushFailedMessages(userId, messages);
                            return null;
                        }
                );
            }
        }
    }
}
