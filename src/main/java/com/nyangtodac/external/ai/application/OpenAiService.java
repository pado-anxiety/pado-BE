package com.nyangtodac.external.ai.application;

import com.nyangtodac.chat.domain.MessageContext;
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

    public OpenAiChatResponse getChatResponse(MessageContext messageContext) {
        ChatCompletionResponse response = openAiClient.sendChatRequest(chatCompletionFactory.buildChatRequest(messageContext));
        return ChatCompletionResponseConverter.convert(response);
    }

}
