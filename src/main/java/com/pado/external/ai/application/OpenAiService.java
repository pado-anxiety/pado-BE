package com.pado.external.ai.application;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChatSummary;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.external.ai.application.response.OpenAiActRecommendationResponse;
import com.pado.external.ai.application.response.OpenAiChatResponse;
import com.pado.external.ai.application.response.OpenAiSummaryResponse;
import com.pado.external.ai.infrastructure.ChatCompletionRequestFactory;
import com.pado.external.ai.infrastructure.ChatCompletionResponse;
import com.pado.external.ai.infrastructure.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiClient openAiClient;
    private final ChatCompletionRequestFactory chatCompletionFactory;
    private final ChatCompletionResponseConverter converter;

    public OpenAiChatResponse getChatResponse(ChattingContext chattingContext, ChatSummaries summaries) {
        ChatCompletionResponse response = openAiClient.sendChatRequest(chatCompletionFactory.buildChatRequest(chattingContext, summaries));
        return converter.convertToChat(response);
    }

    public OpenAiSummaryResponse getChatSummary(List<Chatting> chattings) {
        ChatCompletionResponse response = openAiClient.sendSummaryRequest(chatCompletionFactory.buildSummaryRequest(chattings));
        return converter.convertToSummary(response);
    }

    public Optional<OpenAiActRecommendationResponse> getACTRecommendation(ChatSummary chatSummary) {
        ChatCompletionResponse response = openAiClient.sendACTRecommendRequest(chatCompletionFactory.buildACTRecommendRequest(chatSummary));
        return converter.convertToACTRecommendation(response);
    }
}
