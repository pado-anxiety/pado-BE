package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChattingDBRepository {
    List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n);

    List<Message> findRecentMessages(Long userId, int n);

    CompletableFuture<Void> asyncBatch(Long userId, List<Chatting> chattings);
}
