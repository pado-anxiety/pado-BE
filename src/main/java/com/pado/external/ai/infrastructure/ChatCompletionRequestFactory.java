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
    private final ChatCompletionMessageConverter messageConverter;
    private final ChatOpenAiProperties chatOpenAiProperties;
    private final ChatSummaryOpenAiProperties chatSummaryOpenAiProperties;
    private final ACTRecommendOpenAiProperties actRecommendOpenAiProperties;

    public ChatCompletionRequest buildChatRequest(ChattingContext context, ChatSummaries summaries) {
        ChatCompletionRequest.Builder builder = ChatCompletionRequest.builder()
                .model(chatOpenAiProperties.getModel())
                .systemMessage(promptManager.getChatSystemPrompt().getSystem())
                .messages(messageConverter.convert(context))
                .temperature(chatOpenAiProperties.getTemperature())
                .maxTokens(chatOpenAiProperties.getMaxTokens());

        promptManager.makeSummaryPrompt(summaries).ifPresent(builder::systemMessage);

        return builder.build();
    }

    public ChatCompletionStreamRequest buildStreamChatRequest(ChattingContext context, ChatSummaries summaries) {
        ChatCompletionRequest req = buildChatRequest(context, summaries);
        return new ChatCompletionStreamRequest(req.getModel(), req.getMessages().stream().map(r -> new ChatCompletionStreamRequest.Message(r.getRole(), r.getContent())).toList(), req.getTemperature(), req.getMax_tokens());
    }

    public ChatCompletionRequest buildSummaryRequest(List<Chatting> chattings) {
        return ChatCompletionRequest.builder()
                .model(chatSummaryOpenAiProperties.getModel())
                .systemMessage(promptManager.getSummarySystemPrompt().getSystem())
                .messages(messageConverter.convert(chattings))
                .temperature(chatSummaryOpenAiProperties.getTemperature())
                .maxTokens(chatSummaryOpenAiProperties.getMaxTokens())
                .build();
    }

    public ChatCompletionRequest buildACTRecommendRequest(ChatSummary chatSummary) {
        return ChatCompletionRequest.builder()
                .model(actRecommendOpenAiProperties.getModel())
                .systemMessage(promptManager.getActRecommendPrompt().getSystem())
                .messages(List.of(ChatCompletionRequest.Message.user(chatSummary.getSummaryText())))
                .temperature(actRecommendOpenAiProperties.getTemperature())
                .maxTokens(actRecommendOpenAiProperties.getMaxTokens())
                .build();
    }
}
