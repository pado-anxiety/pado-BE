package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.MessageContext;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.config.properties.ACTRecommendOpenAiProperties;
import com.nyangtodac.config.properties.ChatOpenAiProperties;
import com.nyangtodac.config.properties.ChatSummaryOpenAiProperties;
import com.nyangtodac.external.ai.infrastructure.prompt.PromptManager;
import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({ChatOpenAiProperties.class, ChatSummaryOpenAiProperties.class, ACTRecommendOpenAiProperties.class})
public class ChatCompletionRequestFactory {

    private final PromptManager promptManager;
    private final ChatOpenAiProperties chatOpenAiProperties;
    private final ChatSummaryOpenAiProperties chatSummaryOpenAiProperties;
    private final ACTRecommendOpenAiProperties actRecommendOpenAiProperties;

    public ChatCompletionRequest buildChatRequest(MessageContext context, ChatSummaries summaries) {
        SystemPrompt systemPrompt = promptManager.getChatSystemPrompt();

        List<ChatCompletionRequest.Message> summaryMessages = summaries.getSummaryList().isEmpty()
                ? List.of()
                : List.of(new ChatCompletionRequest.Message(
                "system",
                promptManager.getSummaryPrefix() + summaries.getSummaryList().stream()
                        .map(ChatSummary::getSummaryText)
                        .collect(Collectors.joining("\n\n"))
        ));

        return ChatCompletionRequest.toChatRequest(
                chatOpenAiProperties.getModel(),
                systemPrompt,
                summaryMessages,
                toChatMessages(context.getChattings()),
                chatOpenAiProperties.getTemperature(),
                chatOpenAiProperties.getMaxTokens()
        );
    }

    public ChatCompletionRequest buildSummaryRequest(List<Chatting> chattings) {
        SystemPrompt systemPrompt = promptManager.getSummarySystemPrompt();
        return ChatCompletionRequest.toSummaryRequest(
                chatSummaryOpenAiProperties.getModel(),
                systemPrompt,
                toChatMessages(chattings),
                chatSummaryOpenAiProperties.getTemperature(),
                chatSummaryOpenAiProperties.getMaxTokens()
        );
    }

    public ChatCompletionRequest buildACTRecommendRequest(ChatSummary chatSummary) {
        SystemPrompt systemPrompt = promptManager.getActRecommendPrompt();
        return ChatCompletionRequest.toActRecommendRequest(
                actRecommendOpenAiProperties.getModel(),
                systemPrompt,
                toMessages(chatSummary),
                actRecommendOpenAiProperties.getTemperature(),
                actRecommendOpenAiProperties.getMaxTokens()
        );
    }

    private List<ChatCompletionRequest.Message> toChatMessages(List<Chatting> chattings) {
        return chattings.stream()
                .map(msg -> new ChatCompletionRequest.Message(
                        Sender.valueOf(msg.getSender().toUpperCase()).getRole(),
                        msg.getMessage()
                ))
                .toList();
    }

    private List<ChatCompletionRequest.Message> toMessages(ChatSummary chatSummary) {
        return List.of(new ChatCompletionRequest.Message("user", chatSummary.getSummaryText()));
    }
}
