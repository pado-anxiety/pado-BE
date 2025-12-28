package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.domain.Chatting;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ChattingDBRepository {
    List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n);

    List<Chatting> findRecentMessages(Long userId, int n);

    CompletableFuture<Void> asyncBatch(Long userId, List<Chatting> chattings);

    List<Chatting> findChattingsAfterTsidOrderByTsidAsc(Long userId, Long latestTsid, int limit);
}
