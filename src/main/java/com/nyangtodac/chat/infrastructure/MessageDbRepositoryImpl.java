package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.application.Message;
import com.nyangtodac.chat.controller.dto.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class MessageDbRepositoryImpl implements MessageDbRepository {

    private final MessageJpaRepository messageJpaRepository;

    @Override
    public List<Message> findTopNByUserIdOrderByTsidDesc(Long userId, int n) {
        return messageJpaRepository.findTopNByUserIdOrderByTsidDesc(userId, PageRequest.of(0, n));
    }

    @Override
    @Async("dbTaskExecutor")
    public CompletableFuture<Void> asyncBatch(Long userId, List<Message> messages) {
        return CompletableFuture.runAsync(() -> {
            if (!messages.isEmpty()) {
                List<MessageEntity> entities = messages.stream().map(m -> new MessageEntity(m.getTsid(), userId, Sender.valueOf(m.getRole()), m.getContent())).toList();
                messageJpaRepository.saveAll(entities);
            }
        });
    }
}
