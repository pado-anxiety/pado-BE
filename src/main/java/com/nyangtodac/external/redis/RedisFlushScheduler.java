package com.nyangtodac.external.redis;

import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import com.nyangtodac.chat.infrastructure.ChattingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisFlushScheduler {

    private final ChattingRedisRepository chattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void flushRemainingMessages() {
        List<Long> activeUserIds = chattingRedisRepository.getActiveUserIds();

        for (Long userId : activeUserIds) {
            List<Chatting> chattings = chattingRedisRepository.flushAllMessagesByUserId(userId);
            if (!chattings.isEmpty()) {
                chattingDBRepository.asyncBatch(userId, chattings).exceptionally(
                        e -> {
                            log.error("Failed to flush [remaining messages] for userId: {}", userId, e);
                            chattingRedisRepository.saveFlushFailedMessages(userId, chattings);
                            return null;
                        }
                );
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void retryFailedFlushMessages() {
        List<Long> flushFailedUserIds = chattingRedisRepository.getFlushFailedUserIds();

        for (Long userId : flushFailedUserIds) {
            List<Chatting> chattings = chattingRedisRepository.flushFailedMessagesByUserId(userId);
            if (!chattings.isEmpty()) {
                chattingDBRepository.asyncBatch(userId, chattings).exceptionally(
                        e -> {
                            log.error("Failed to flush [flush failed messages] for userId: {}", userId, e);
                            chattingRedisRepository.saveFlushFailedMessages(userId, chattings);
                            return null;
                        }
                );
            }
        }
    }
}
