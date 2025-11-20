package com.nyangtodac.external.ai.application;

import com.nyangtodac.chat.application.RecentMessages;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequestFactory;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionResponse;
import com.nyangtodac.external.ai.infrastructure.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiClient openAiClient;
    private final ChatCompletionRequestFactory chatCompletionFactory;

    public OpenAiChatResponse getChatResponse(RecentMessages recentMessages, String userMessage) {
        ChatCompletionResponse response = openAiClient.sendRequest(chatCompletionFactory.buildChatRequest(recentMessages.asString(), userMessage));
        return ChatCompletionResponseConverter.convert(response);
    }
}
