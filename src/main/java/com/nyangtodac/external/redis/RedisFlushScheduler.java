package com.nyangtodac.external.redis;

import com.nyangtodac.chat.application.Message;
import com.nyangtodac.chat.infrastructure.MessageDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisFlushScheduler {

    private final MessageRedisRepository messageRedisRepository;
    private final MessageDbRepository messageDbRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void flushRemainingChats() {
        List<Long> userIds = messageRedisRepository.getActiveUserIds();

        for (Long userId : userIds) {
            List<Message> messages = messageRedisRepository.flushAllMessagesByUserId(userId);
            if (!messages.isEmpty()) {
                messageDbRepository.asyncFlush(userId, messages);
            }
        }
    }
}
