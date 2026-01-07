package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nyangtodac.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecentChattingRedisRepository {

    private static final String CHAT_PREFIX = "chat:";

    private final StringRedisTemplate redisTemplate;
    private final ChattingSerializer chattingSerializer;

    @Value("${chat.context.size}") private int contextSize;
    @Value("${chat.context.ttl-minutes}") private int ttlMinutes;

    public void appendContextCache(Long userId, List<Chatting> chattings) {
        if (chattings == null || chattings.isEmpty()) return;

        String key = CHAT_PREFIX + userId;

        chattings.stream()
                .sorted(Comparator.comparing(Chatting::getTsid))
                .forEach(chatting -> {
                    try {
                        String json = chattingSerializer.serialize(chatting);
                        redisTemplate.opsForList().rightPush(key, json);
                    } catch (JsonProcessingException e) {
                        log.warn("context serialize failed. userId={}", userId);
                    }
                });

        redisTemplate.opsForList().trim(key, -contextSize, -1);
        redisTemplate.expire(key, Duration.ofMinutes(ttlMinutes));
    }

    public List<Chatting> getRecentChattings(Long userId) {
        String key = CHAT_PREFIX + userId;

        List<String> jsons = redisTemplate.opsForList().range(key, -contextSize, -1);
        List<Chatting> result = new ArrayList<>();

        if (!jsons.isEmpty()) {
            for (String json : jsons) {
                try {
                    Optional<Chatting> chatting = chattingSerializer.deserialize(json);
                    chatting.ifPresent(result::add);
                } catch (JsonProcessingException e) {
                    log.warn("context deserialize failed. userId={}", userId);
                }
            }
        }

        return result;
    }


}
