package com.nyangtodac.external.ai.infrastructure.prompt;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PromptManager {

    private final SystemPrompt chatSystemPrompt;

    public PromptManager() {
        String chatTemplatePath = "prompts/chat_prompt.yaml";
        chatSystemPrompt = YamlResourceLoader.load(chatTemplatePath, SystemPrompt.class);
    }

}
