package com.nyangtodac.chat.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nyangtodac.chat.application.Chatting;
import com.nyangtodac.chat.application.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChattingDBRepositoryImpl implements ChattingDBRepository {

    private final ChattingJpaRepository chattingJpaRepository;
    private final ChattingSerializer chattingSerializer;
    private final MessageDeserializer messageDeserializer;

    @Override
    public List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n) {
        List<ChattingEntity> entities = chattingJpaRepository.findTopNByUserIdAndTsidLessThanOrderByTsidDesc(userId, cursor, PageRequest.of(0, n));
        return entities.stream()
                .map(ChattingEntity::getJsonPayload)
                .map(base64 -> {
                    try {
                        return chattingSerializer.deserialize(base64);
                    } catch (JsonProcessingException e) {
                        log.warn("Chatting 역직렬화 실패, userId={}, payload={}", userId, base64, e);
                        return Optional.<Chatting>empty();
                    }
                })
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<Message> findRecentMessages(Long userId, int n) {
        List<ChattingEntity> entities = chattingJpaRepository.findTopNByUserIdOrderByTsidDesc(userId, PageRequest.of(0, n));
        return entities.stream()
                .map(ChattingEntity::getJsonPayload)
                .map(messageDeserializer::deserialize)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    @Async("dbTaskExecutor")
    public CompletableFuture<Void> asyncBatch(Long userId, List<Chatting> chattings) {
        return CompletableFuture.runAsync(() -> {
            if (!chattings.isEmpty()) {
                List<ChattingEntity> entities = chattings.stream().map(chatting -> {
                    try {
                        return new ChattingEntity(chatting.getTsid(), userId, chattingSerializer.serialize(chatting));
                    } catch (JsonProcessingException e) {
                        log.error("tsid 추출 실패, userId={} message={}", userId, chatting, e);
                        throw new RuntimeException(e);
                    }
                }).toList();


                chattingJpaRepository.saveAll(entities);
            }
        });
    }
}
