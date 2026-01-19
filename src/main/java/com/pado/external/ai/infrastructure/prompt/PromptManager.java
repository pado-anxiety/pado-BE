package com.pado.external.ai.infrastructure.prompt;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChatSummary;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Getter
@Component
public class PromptManager {

    private final SystemPrompt chatSystemPrompt;
    private final SystemPrompt summarySystemPrompt;
    private final SystemPrompt summaryPrefix;
    private final SystemPrompt actRecommendPrompt;

    public PromptManager() {
        chatSystemPrompt = YamlResourceLoader.load("prompts/chat_prompt.yaml", SystemPrompt.class);
        summarySystemPrompt = YamlResourceLoader.load("prompts/summary_prompt.yaml", SystemPrompt.class);
        summaryPrefix = YamlResourceLoader.load("prompts/summary_prefix.yaml", SystemPrompt.class);
        actRecommendPrompt = YamlResourceLoader.load("prompts/act_recommend_prompt.yaml", SystemPrompt.class);
    }

    public String makeSummaryPrompt(ChatSummaries summaries) {
        return summaryPrefix.getSystem() + "\n" + summaries.getSummaryList().stream()
                .map(ChatSummary::getSummaryText)
                .collect(Collectors.joining("\n\n"));
    }

}
