package com.nyangtodac.external.ai.application;

import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.MessageContext;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.application.response.OpenAiSummaryResponse;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequestFactory;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.infrastructure.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiClient openAiClient;
    private final ChatCompletionRequestFactory chatCompletionFactory;

    public OpenAiChatResponse getChatResponse(MessageContext messageContext, ChatSummaries summaries) {
        ChatCompletionResponse response = openAiClient.sendChatRequest(chatCompletionFactory.buildChatRequest(messageContext, summaries));
        return ChatCompletionResponseConverter.convertToChat(response);
    }

    public OpenAiSummaryResponse getChatSummary(List<Chatting> chattings) {
        ChatCompletionResponse response = openAiClient.sendSummaryRequest(chatCompletionFactory.buildSummaryRequest(chattings));
        return ChatCompletionResponseConverter.convertToSummary(response);
    }

}
