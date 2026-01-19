package com.pado.external.ai.infrastructure;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChatSummary;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.config.properties.ACTRecommendOpenAiProperties;
import com.pado.config.properties.ChatOpenAiProperties;
import com.pado.config.properties.ChatSummaryOpenAiProperties;
import com.pado.external.ai.infrastructure.prompt.PromptManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({ChatOpenAiProperties.class, ChatSummaryOpenAiProperties.class, ACTRecommendOpenAiProperties.class})
public class ChatCompletionRequestFactory {

    private final PromptManager promptManager;
    private final ChatOpenAiProperties chatOpenAiProperties;
    private final ChatSummaryOpenAiProperties chatSummaryOpenAiProperties;
    private final ACTRecommendOpenAiProperties actRecommendOpenAiProperties;

    public ChatCompletionRequest buildChatRequest(ChattingContext context, ChatSummaries summaries) {
        String systemPrompt = promptManager.getChatSystemPrompt().getSystem();
        String summaryPrompt = promptManager.makeSummaryPrompt(summaries);

        return ChatCompletionRequest.toChatRequest(
                chatOpenAiProperties.getModel(),
                systemPrompt,
                summaryPrompt,
                context,
                chatOpenAiProperties.getTemperature(),
                chatOpenAiProperties.getMaxTokens()
        );
    }

    public ChatCompletionRequest buildSummaryRequest(List<Chatting> chattings) {
        String systemPrompt = promptManager.getSummarySystemPrompt().getSystem();
        return ChatCompletionRequest.toSummaryRequest(
                chatSummaryOpenAiProperties.getModel(),
                systemPrompt,
                chattings,
                chatSummaryOpenAiProperties.getTemperature(),
                chatSummaryOpenAiProperties.getMaxTokens()
        );
    }

    public ChatCompletionRequest buildACTRecommendRequest(ChatSummary chatSummary) {
        String systemPrompt = promptManager.getActRecommendPrompt().getSystem();
        return ChatCompletionRequest.toActRecommendRequest(
                actRecommendOpenAiProperties.getModel(),
                systemPrompt,
                chatSummary,
                actRecommendOpenAiProperties.getTemperature(),
                actRecommendOpenAiProperties.getMaxTokens()
        );
    }
}
