package com.nyangtodac.external.ai.application;

import com.nyangtodac.chat.application.RecentMessages;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequest;
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

    public OpenAiChatResponse getChatResponse(RecentMessages recentMessages) {
        ChatCompletionResponse response = openAiClient.sendRequest(chatCompletionFactory.buildChatRequest(toChatMessages(recentMessages)));
        return ChatCompletionResponseConverter.convert(response);
    }

    private List<ChatCompletionRequest.Message> toChatMessages(RecentMessages recent) {
        return recent.getMessages().stream()
                .map(msg -> new ChatCompletionRequest.Message(
                        msg.getRole().toLowerCase(),
                        msg.getContent()
                ))
                .toList();
    }
}
