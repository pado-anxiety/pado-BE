package com.nyangtodac.external.ai.infrastructure.prompt;

import org.springframework.stereotype.Component;

@Component
public class PromptManager {

    private final Prompt chatPrompt;

    public PromptManager() {
        String chatTemplatePath = "prompts/chat_prompt.yaml";
        chatPrompt = YamlResourceLoader.load(chatTemplatePath, Prompt.class);
    }

    public Prompt getChatPrompt(String recentMessages, String userMessage) {
        return new Prompt(chatPrompt.getSystem(), userMessage, recentMessages);
    }
}
