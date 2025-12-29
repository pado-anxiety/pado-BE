package com.nyangtodac.act.recommend;

import com.nyangtodac.chat.application.ChattingService;
import com.nyangtodac.chat.application.ConversationSummaryService;
import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.RecentChattings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public ACTRecommendation recommend(Long userId) {
        Optional<ChatSummary> summary = getSummaryForRecommendation(userId);
        if (summary.isEmpty()) {
            return actRecommender.getDefaultRecommendation();
        }
        return actRecommender.getRecommendation(summary.get());
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
