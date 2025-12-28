package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChattingDBRepositoryImpl implements ChattingDBRepository {

    private final ChattingJpaRepository chattingJpaRepository;

    @Override
    public List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n) {
        return chattingJpaRepository.findTopNByUserIdAndTsidLessThanOrderByTsidDesc(userId, cursor, PageRequest.of(0, n)).stream().map(ChattingEntity::toModel).toList();
    }

    @Override
    public List<Chatting> findRecentMessages(Long userId, int n) {
        return chattingJpaRepository.findTopNByUserIdOrderByTsidDesc(userId, PageRequest.of(0, n)).stream().map(ChattingEntity::toModel).toList();
    }

    @Override
    @Async("dbTaskExecutor")
    public CompletableFuture<Void> asyncBatch(Long userId, List<Chatting> chattings) {
        return CompletableFuture.runAsync(() -> {
            if (!chattings.isEmpty()) {
                List<ChattingEntity> entities = chattings.stream().map(c -> ChattingEntity.fromModel(userId, c)).toList();
                chattingJpaRepository.saveAll(entities);
            }
        });
    }

    @Override
    public List<Chatting> findChattingsAfterTsid(Long userId, Long latestTsid, int limit) {
        return chattingJpaRepository.findTopNByUserIdAndTsidGreaterThanOrderByTsidAsc(userId, latestTsid, PageRequest.of(0, limit)).stream().map(ChattingEntity::toModel).toList();
    }
}
