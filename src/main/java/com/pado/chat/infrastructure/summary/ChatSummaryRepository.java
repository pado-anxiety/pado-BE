package com.pado.chat.infrastructure.summary;

import com.pado.chat.domain.ChatSummary;

import java.util.List;

public interface ChatSummaryRepository {
    List<ChatSummary> findLatestSummariesByUserId(Long userId, int limit);

    void save(Long userId, ChatSummary chatSummary);
}
