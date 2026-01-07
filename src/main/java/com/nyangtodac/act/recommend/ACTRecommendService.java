package com.nyangtodac.act.recommend;

import com.nyangtodac.act.recommend.infrastructure.AiACTRecommendationRepository;
import com.nyangtodac.chat.application.ChattingService;
import com.nyangtodac.chat.application.ConversationSummaryService;
import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.RecentChattings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ACTRecommendService {

    private final ConversationSummaryService summaryService;
    private final ChattingService chattingService;
    private final ACTRecommender actRecommender;
    private final AiACTRecommendationRepository aiActRecommendationRepository;

    @Value("${act.recommend.quota.max-tokens}") private int recommendQuota;

    @Transactional
    public ACTRecommendation recommend(Long userId) {
        if (aiActRecommendationRepository.countTodayAiRecommended(userId) >= recommendQuota) {
            throw new ACTRecommendQuotaExceededException();
        }
        Optional<ChatSummary> summary = getSummaryForRecommendation(userId);
        if (summary.isEmpty()) {
            return actRecommender.getDefaultRecommendation();
        }
        ACTRecommendation recommendation = actRecommender.getRecommendation(summary.get());
        aiActRecommendationRepository.saveAiRecommend(userId, recommendation);
        return recommendation;
    }

    private Optional<ChatSummary> getSummaryForRecommendation(Long userId) {
        ChatSummaries summaries = summaryService.getConversationSummaries(userId, 1);
        if (!summaries.getSummaryList().isEmpty()) {
            return Optional.of(summaries.getSummaryList().get(0));
        } else {
            RecentChattings recentChattings = chattingService.getRecentChattingsBeforeCursor(userId, null);
            if (recentChattings.getChattings().size() >= 10) {
                List<Chatting> chattings = new ArrayList<>(recentChattings.getChattings().subList(0, 10));
                Collections.reverse(chattings); //오래된 -> 최신
                return Optional.of(summaryService.summarize(chattings));
            }
        }
        return Optional.empty();
    }
}
