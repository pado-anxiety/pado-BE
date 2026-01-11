package com.pado.external.ai.infrastructure.prompt;

import lombok.Getter;
import org.springframework.stereotype.Component;

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

}
