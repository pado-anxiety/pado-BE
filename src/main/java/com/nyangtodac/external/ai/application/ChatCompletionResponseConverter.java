package com.nyangtodac.external.ai.application;

import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.OpenAiException;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;

public class ChatCompletionResponseConverter {

    public static OpenAiChatResponse convert(ChatCompletionResponse response) {
        String content = getContentFrom(response);
        return new OpenAiChatResponse(content.lines().map(String::trim).filter(s -> !s.isEmpty()).toList());
    }

    private static String getContentFrom(ChatCompletionResponse response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()) {
            throw new OpenAiException("OpenAI 응답이 비어있습니다.");
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}
