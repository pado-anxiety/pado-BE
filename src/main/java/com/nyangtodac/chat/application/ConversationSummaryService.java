package com.nyangtodac.chat.application;

import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.infrastructure.summary.ChatSummaryRepository;
import com.nyangtodac.external.ai.application.OpenAiService;
import com.nyangtodac.external.ai.application.response.OpenAiSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationSummaryService {

    private final ChattingService chattingService;
    private final ChatSummaryRepository chatSummaryRepository;
    private final OpenAiService openAiService;

    private static final int SUMMARIZE_THRESHOLD = 30;
    private final ConcurrentHashMap<Long, Object> lock = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    public ChatSummaries getConversationSummaries(Long userId, int limit) {
        List<ChatSummary> summaries = chatSummaryRepository.findLatestSummariesByUserId(userId, limit);
        return new ChatSummaries(summaries);
    }

    @Async("summaryExecutor")
    public void asyncSummarize(Long userId) {
        if (!tryLock(userId)) {
            log.debug("Summarize skipped: user {} already locked", userId);
            return;
        }
        try {
            long latestTsid = 0L;
            List<ChatSummary> summary = chatSummaryRepository.findLatestSummariesByUserId(userId, 1);
            if (!summary.isEmpty()) {
                latestTsid = summary.get(0).getToTsid();
            }

            List<Chatting> chattings = chattingService.getRecentChattingsAfterCursorOrderByTsidAscFromDB(userId, latestTsid, SUMMARIZE_THRESHOLD);
            if (chattings.isEmpty() || chattings.size() < SUMMARIZE_THRESHOLD) {
                return;
            }
            Long fromTsid = chattings.get(0).getTsid();
            Long toTsid = chattings.get(chattings.size() - 1).getTsid();

            OpenAiSummaryResponse openAiSummary = openAiService.getChatSummary(chattings);
            chatSummaryRepository.save(userId, new ChatSummary(fromTsid, toTsid, openAiSummary.getSummaryText()));
        } finally {
            unlock(userId);
        }

    }

    private boolean tryLock(Long userId) {
        return lock.putIfAbsent(userId, new Object()) == null;
    }

    private void unlock(Long userId) {
        lock.remove(userId);
    }
}
