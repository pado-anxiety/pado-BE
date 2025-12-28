package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.chat.domain.MessageContext;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.config.properties.ChatOpenAiProperties;
import com.nyangtodac.external.ai.infrastructure.prompt.PromptManager;
import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ChatOpenAiProperties.class)
public class ChatCompletionRequestFactory {

    private final PromptManager promptManager;
    private final ChatOpenAiProperties chatOpenAiProperties;

    public ChatCompletionRequest buildChatRequest(MessageContext context) {
        SystemPrompt systemPrompt = promptManager.getChatSystemPrompt();
        return new ChatCompletionRequest(
                chatOpenAiProperties.getModel(),
                systemPrompt,
                toChatMessages(context),
                chatOpenAiProperties.getTemperature(),
                chatOpenAiProperties.getMaxTokens()
        );
    }

    private List<ChatCompletionRequest.Message> toChatMessages(MessageContext recent) {
        return recent.getChattings().stream()
                .map(msg -> new ChatCompletionRequest.Message(
                        Sender.valueOf(msg.getSender().toUpperCase()).getRole(),
                        msg.getMessage()
                ))
                .toList();
    }
}
