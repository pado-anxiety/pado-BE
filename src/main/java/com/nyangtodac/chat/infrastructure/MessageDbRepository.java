package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.application.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MessageDbRepository {
    List<Message> findTopNByUserIdOrderByCreatedAtDescIdDesc(Long userId, int n);

    CompletableFuture<Void> asyncBatch(Long userId, List<Message> messages);
}
