package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.infrastructure.prompt.Prompt;
import com.nyangtodac.external.ai.infrastructure.prompt.PromptManager;
import org.springframework.stereotype.Component;

@Component
public class ChatCompletionRequestFactory {

    private static final String model = "gpt-4o-mini";
    private static final double temperature = 0.7;
    private static final int maxToken = 400;

    private final PromptManager promptManager;

    public ChatCompletionRequestFactory(PromptManager promptManager) {
        this.promptManager = promptManager;
    }

    public ChatCompletionRequest buildChatRequest(String recentMessages, String userMessage) {
        Prompt prompt = promptManager.getChatPrompt(recentMessages, userMessage);
        return new ChatCompletionRequest(model, prompt, temperature, maxToken);
    }
}
