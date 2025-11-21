package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.chat.application.MessageContext;
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

    public ChatCompletionRequest buildChatRequest(MessageContext context) {
        SystemPrompt systemPrompt = promptManager.getChatSystemPrompt();
        return new ChatCompletionRequest(model, systemPrompt, toChatMessages(context), temperature, maxToken);
    }

    private List<ChatCompletionRequest.Message> toChatMessages(MessageContext recent) {
        return recent.getMessages().stream()
                .map(msg -> new ChatCompletionRequest.Message(
                        msg.getRole().toLowerCase(),
                        msg.getContent()
                ))
                .toList();
    }
}
