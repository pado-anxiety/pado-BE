package com.pado.chat.application;

import com.pado.external.ai.application.OpenAiStreamService;
import com.pado.external.ai.infrastructure.ChatCompletionRequest;
import com.pado.external.ai.infrastructure.ChatCompletionRequestFactory;
import com.pado.external.ai.infrastructure.ChatCompletionStreamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AIChatStreamingService {

    private final ChatCompletionRequestFactory chatCompletionFactory;
    private final OpenAiStreamService openAiStreamService;

    public Flux<String> postMessage(ChatPreProcessResult preProcessResult) {
        ChatCompletionRequest req = chatCompletionFactory.buildChatRequest(preProcessResult.getChattingContext(), preProcessResult.getChatSummaries());
        return openAiStreamService.getChatResponse(new ChatCompletionStreamRequest(req.getModel(), req.getMessages().stream().map(r -> new ChatCompletionStreamRequest.Message(r.getRole(), r.getContent())).toList(), req.getTemperature(), req.getMax_tokens()));
    }
}
