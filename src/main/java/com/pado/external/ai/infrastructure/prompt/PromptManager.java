package com.pado.external.ai.infrastructure.prompt;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChatSummary;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Component
public class PromptManager {

    private final SystemPrompt chatSystemPrompt;
    private final SystemPrompt summarySystemPrompt;
    private final SystemPrompt summaryPrefix;
    private final SystemPrompt actRecommendPrompt;

    public PromptManager(SystemPrompt chatSystemPrompt, SystemPrompt summarySystemPrompt, SystemPrompt summaryPrefix, SystemPrompt actRecommendPrompt) {
        this.chatSystemPrompt = chatSystemPrompt;
        this.summarySystemPrompt = summarySystemPrompt;
        this.summaryPrefix = summaryPrefix;
        this.actRecommendPrompt = actRecommendPrompt;
    }

    public Optional<String> makeSummaryPrompt(ChatSummaries summaries) {
        if (summaries.getSummaryList().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(summaryPrefix.getSystem() + "\n" + summaries.getSummaryList().stream()
                .map(ChatSummary::getSummaryText)
                .collect(Collectors.joining("\n\n")));
    }

}
