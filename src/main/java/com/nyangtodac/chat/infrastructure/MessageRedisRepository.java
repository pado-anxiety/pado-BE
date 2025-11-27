package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyangtodac.chat.application.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRedisRepository {

    private static final String CHAT_MESSAGES_PREFIX = "chat:";
    private static final String CHAT_MESSAGES_SUFFIX = ":messages";

    private static final String FLUSH_FAILED_MESSAGES_PREFIX = "flush_fail:";
    private static final String FLUSH_FAILED_MESSAGES_SUFFIX = ":messages";

    private static final int CHAT_MESSAGES_MAX_SIZE = 4;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("rawtypes")
    private final DefaultRedisScript<List> pushAndTrimScript = new DefaultRedisScript<>();

    @SuppressWarnings("rawtypes")
    private final DefaultRedisScript<List> flushAllScript = new DefaultRedisScript<>();

    @PostConstruct
    public void init() {
        try {
            pushAndTrimScript.setScriptText(Files.readString(Path.of(new ClassPathResource("scripts/push_and_trim.lua").getURI()), StandardCharsets.UTF_8));
            pushAndTrimScript.setResultType(List.class);
            flushAllScript.setScriptText(Files.readString(Path.of(new ClassPathResource("scripts/flush_all.lua").getURI()), StandardCharsets.UTF_8));
            flushAllScript.setResultType(List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> saveMessages(Long userId, List<Message> messages) {
        if (messages.isEmpty()) return Collections.emptyList();

        List<String> serialized = messages.stream().map(this::serialize).toList();

        List<String> args = new ArrayList<>(serialized);
        args.add(String.valueOf(CHAT_MESSAGES_MAX_SIZE));

        @SuppressWarnings("unchecked")
        List<String> overflowMessages = (List<String>) redisTemplate.execute(pushAndTrimScript, List.of(generateKey(userId)), args.toArray());

        if (!overflowMessages.isEmpty()) {
            return overflowMessages.stream().map(this::deserialize).toList();
        }
        return Collections.emptyList();
    }

    public void saveFlushFailedMessages(Long userId, List<Message> messages) {
        List<String> serialized = messages.stream().map(this::serialize).toList();
        redisTemplate.opsForList().rightPushAll(FLUSH_FAILED_MESSAGES_PREFIX + userId + FLUSH_FAILED_MESSAGES_SUFFIX, serialized);
    }

    public List<Message> findRecentMessages(Long userId, int n) {
        String key = generateKey(userId);

        long size = redisTemplate.opsForList().size(key);
        if (size == 0) {
            return Collections.emptyList();
        }

        long start = Math.max(size - n, 0);
        long end = size - 1;

        List<String> strings = redisTemplate.opsForList().range(key, start, end);
        List<Message> messages = new ArrayList<>();
        for (String json : strings) {
            messages.add(deserialize(json));
        }

        return messages;
    }

    public List<Long> getActiveUserIds() {
        return redisTemplate
                .keys(CHAT_MESSAGES_PREFIX + "*" + CHAT_MESSAGES_SUFFIX)
                .stream()
                .map(s -> s.replace(CHAT_MESSAGES_PREFIX, "").replace(CHAT_MESSAGES_SUFFIX, ""))
                .map(Long::valueOf)
                .toList();
    }

    public List<Long> getFlushFailedUserIds() {
        return redisTemplate
                .keys(CHAT_MESSAGES_PREFIX + "*" + CHAT_MESSAGES_SUFFIX)
                .stream()
                .map(s -> s.replace(CHAT_MESSAGES_PREFIX, "").replace(CHAT_MESSAGES_SUFFIX, ""))
                .map(Long::valueOf)
                .toList();
    }

    public List<Message> flushAllMessagesByUserId(Long userId) {
        @SuppressWarnings("unchecked")
        List<String> serialized = (List<String>) redisTemplate.execute(flushAllScript, List.of(generateKey(userId)));
        return serialized.stream().map(this::deserialize).toList();
    }

    public List<Message> flushFailedMessagesByUserId(Long userId) {
        @SuppressWarnings("unchecked")
        List<String> serialized = (List<String>) redisTemplate.execute(flushAllScript, List.of(FLUSH_FAILED_MESSAGES_PREFIX + userId + FLUSH_FAILED_MESSAGES_SUFFIX));
        return serialized.stream().map(this::deserialize).toList();
    }

    private String generateKey(Long userId) {
        return CHAT_MESSAGES_PREFIX + userId + CHAT_MESSAGES_SUFFIX;
    }

    private String serialize(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Message deserialize(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
