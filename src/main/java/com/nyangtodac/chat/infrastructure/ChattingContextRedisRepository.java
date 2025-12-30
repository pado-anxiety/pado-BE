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
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChattingContextRedisRepository {

    private static final String CHAT_CONTEXT_PREFIX = "chat:context:";

    private final StringRedisTemplate redisTemplate;
    private final ChattingSerializer chattingSerializer;

    @Value("${chat.context.size}") private int contextSize;
    @Value("${chat.context.ttl-minutes}") private int ttlMinutes;

    public void refreshContextCache(Long userId, List<Chatting> chattings) {
        if (chattings == null || chattings.isEmpty()) return;

        String key = CHAT_CONTEXT_PREFIX + userId;

        for (Chatting chatting : chattings) {
            try {
                String json = chattingSerializer.serialize(chatting);
                redisTemplate.opsForList().leftPush(key, json);
            } catch (JsonProcessingException e) {
                log.warn("load context cache failed. userId={}", userId);
            }
        }

        redisTemplate.opsForList().trim(key, 0, contextSize - 1);
        redisTemplate.expire(key, Duration.ofMinutes(ttlMinutes));
    }

    public List<Chatting> getContext(Long userId) {
        String key = CHAT_CONTEXT_PREFIX + userId;

        List<String> jsons = redisTemplate.opsForList().range(key, 0, contextSize - 1);
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
