package com.nyangtodac.chat.infrastructure.summary;

import com.nyangtodac.chat.domain.ChatSummary;

import java.util.List;

public interface ChatSummaryRepository {
    List<ChatSummary> findLatestSummariesByUserId(Long userId, int limit);

    void save(Long userId, ChatSummary chatSummary);
}
