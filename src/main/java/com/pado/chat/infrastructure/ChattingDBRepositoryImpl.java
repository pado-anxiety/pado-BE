package com.pado.chat.infrastructure;

import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChattingDBRepositoryImpl implements ChattingDBRepository {

    private final ChattingJpaRepository chattingJpaRepository;

    @Override
    public Chatting save(Long userId, Chatting chatting) {
        return chattingJpaRepository.save(ChattingEntity.fromModel(userId, chatting)).toModel();
    }

    @Override
    public List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n) {
        return chattingJpaRepository.findByUserIdAndTsidLessThanOrderByTsidDesc(userId, cursor, PageRequest.of(0, n)).stream().map(ChattingEntity::toModel).toList();
    }

    @Override
    public List<Chatting> findRecentMessages(Long userId, int n) {
        return chattingJpaRepository.findByUserIdOrderByTsidDesc(userId, PageRequest.of(0, n)).stream().map(ChattingEntity::toModel).toList();
    }

    @Override
    public List<Chatting> findChattingsAfterTsidOrderByTsidAsc(Long userId, Long latestTsid, int limit) {
        return chattingJpaRepository.findByUserIdAndTsidGreaterThanOrderByTsidAsc(userId, latestTsid, PageRequest.of(0, limit)).stream().map(ChattingEntity::toModel).toList();
    }
}
