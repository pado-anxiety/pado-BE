package com.pado.chat.infrastructure.summary;

import com.pado.chat.domain.ChatSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatSummaryRepositoryImpl implements ChatSummaryRepository {

    private final ChatSummaryJpaRepository chatSummaryJpaRepository;

    @Override
    public List<ChatSummary> findLatestSummariesByUserId(Long userId, int limit) {
        List<ChatSummaryEntity> entities = chatSummaryJpaRepository.findByUserIdOrderByIdDesc(userId, PageRequest.of(0, limit));
        return entities.stream().map(ChatSummaryEntity::toModel).toList();
    }

    @Override
    public void save(Long userId, ChatSummary chatSummary) {
        chatSummaryJpaRepository.save(ChatSummaryEntity.fromModel(chatSummary, userId));
    }
}
