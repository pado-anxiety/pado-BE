package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.infrastructure.prompt.PromptManager;
import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatCompletionRequestFactory {

    private static final String model = "gpt-4o-mini";
    private static final double temperature = 0.7;
    private static final int maxToken = 550;

    private final PromptManager promptManager;

    public ChatCompletionRequestFactory(PromptManager promptManager) {
        this.promptManager = promptManager;
    }

    public ChatCompletionRequest buildChatRequest(List<ChatCompletionRequest.Message> messages) {
        SystemPrompt systemPrompt = promptManager.getChatSystemPrompt();
        return new ChatCompletionRequest(model, systemPrompt, messages, temperature, maxToken);
    }
}
