package com.nyangtodac.external.ai.application;

import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.application.response.OpenAiSummaryResponse;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.resilience4j.retry.OpenAiClientException;

public class ChatCompletionResponseConverter {

    public static OpenAiChatResponse convertToChat(ChatCompletionResponse response) {
        String content = getContentFrom(response);
        return new OpenAiChatResponse(content);
    }

    public static OpenAiSummaryResponse convertToSummary(ChatCompletionResponse response) {
        String content = getContentFrom(response);
        return new OpenAiSummaryResponse(content);
    }

    private static String getContentFrom(ChatCompletionResponse response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()) {
            throw new OpenAiClientException("OpenAI 응답이 비어있습니다.");
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}
