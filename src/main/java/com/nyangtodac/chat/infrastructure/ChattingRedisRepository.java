package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChattingRedisRepository {

    private static final String CHAT_MESSAGES_PREFIX = "chat:";
    private static final String CHAT_MESSAGES_SUFFIX = ":messages";

    private static final String FLUSH_FAILED_MESSAGES_PREFIX = "flush_fail:";
    private static final String FLUSH_FAILED_MESSAGES_SUFFIX = ":messages";

    private static final int CHAT_MESSAGES_MAX_SIZE = 30;

    private final StringRedisTemplate redisTemplate;
    private final ChattingSerializer chattingSerializer;
    private final MessageDeserializer messageDeserializer;

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

    public List<Chatting> saveMessages(Long userId, List<Chatting> chattings) {
        if (chattings.isEmpty()) return Collections.emptyList();

        List<String> serialized = chattings.stream().map(m -> serialize(userId, m)).toList();

        List<String> args = new ArrayList<>(serialized);
        args.add(String.valueOf(CHAT_MESSAGES_MAX_SIZE));

        @SuppressWarnings("unchecked")
        List<String> overflowMessages = (List<String>) redisTemplate.execute(pushAndTrimScript, List.of(generateKey(userId)), args.toArray());

        if (!overflowMessages.isEmpty()) {
            return overflowMessages.stream().map(o -> deserialize(userId, o)).flatMap(Optional::stream).toList();
        }
        return Collections.emptyList();
    }

    public void saveFlushFailedMessages(Long userId, List<Chatting> chattings) {
        List<String> serialized = chattings.stream().map(m -> serialize(userId, m)).toList();
        redisTemplate.opsForList().rightPushAll(FLUSH_FAILED_MESSAGES_PREFIX + userId + FLUSH_FAILED_MESSAGES_SUFFIX, serialized);
    }

    public List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n) {
        String key = generateKey(userId);

        long size = redisTemplate.opsForList().size(key);
        if (size == 0) {
            return Collections.emptyList();
        }

        long start = Math.max(size - n, 0);
        long end = size - 1;

        List<String> strings = redisTemplate.opsForList().range(key, start, end);
        List<Chatting> chattings = new ArrayList<>();
        for (String json : strings) {
            Optional<Chatting> deserialize = deserialize(userId, json).filter(chatting -> chatting.getTsid() < cursor);
            deserialize.ifPresent(chattings::add);
        }

        return chattings;
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
            Optional<Message> deserialize = messageDeserializer.deserialize(json);
            deserialize.ifPresent(messages::add);
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

    public List<Chatting> flushAllMessagesByUserId(Long userId) {
        @SuppressWarnings("unchecked")
        List<String> serialized = (List<String>) redisTemplate.execute(flushAllScript, List.of(generateKey(userId)));
        return serialized.stream().map(s -> deserialize(userId, s)).flatMap(Optional::stream).toList();
    }

    public List<Chatting> flushFailedMessagesByUserId(Long userId) {
        @SuppressWarnings("unchecked")
        List<String> serialized = (List<String>) redisTemplate.execute(flushAllScript, List.of(FLUSH_FAILED_MESSAGES_PREFIX + userId + FLUSH_FAILED_MESSAGES_SUFFIX));
        return serialized.stream().map(s -> deserialize(userId, s)).flatMap(Optional::stream).toList();
    }

    private String generateKey(Long userId) {
        return CHAT_MESSAGES_PREFIX + userId + CHAT_MESSAGES_SUFFIX;
    }

    private String serialize(Long userId, Chatting chatting) {
        try {
            return chattingSerializer.serialize(chatting);
        } catch (JsonProcessingException e) {
            log.error("Chatting 직렬화 실패, userId={} message={}", userId, chatting, e);
            throw new RuntimeException(e);
        }
    }

    private Optional<Chatting> deserialize(Long userId, String base64) {
        try {
            return chattingSerializer.deserialize(base64);
        } catch (JsonProcessingException e) {
            saveFlushFailedMessagesRaw(userId, List.of(base64));
            log.warn("Message 역직렬화 실패, userId={}", userId);
            return Optional.empty();
        }
    }

    private void saveFlushFailedMessagesRaw(Long userId, List<String> rawMessages) {
        redisTemplate.opsForList().rightPushAll(FLUSH_FAILED_MESSAGES_PREFIX + userId + FLUSH_FAILED_MESSAGES_SUFFIX, rawMessages);
    }
}
