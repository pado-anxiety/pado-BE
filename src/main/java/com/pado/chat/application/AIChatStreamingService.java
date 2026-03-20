package com.pado.chat.application;

import com.pado.external.ai.application.OpenAiStreamService;
import com.pado.external.ai.infrastructure.ChatCompletionRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AIChatStreamingService {

    private final ChatCompletionRequestFactory chatCompletionFactory;
    private final OpenAiStreamService openAiStreamService;

    public Flux<String> postMessage(ChatPreProcessResult preProcessResult) {
        return openAiStreamService.getChatResponse(
                chatCompletionFactory.buildStreamChatRequest(
                        preProcessResult.getChattingContext(), preProcessResult.getChatSummaries()
                )
        );
    }
}
