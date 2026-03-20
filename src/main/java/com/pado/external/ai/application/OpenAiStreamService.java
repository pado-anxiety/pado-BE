package com.pado.external.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.external.ai.infrastructure.ChatCompletionStreamRequest;
import com.pado.external.ai.infrastructure.OpenAiStreamClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiStreamService {

    private final ObjectMapper objectMapper;
    private final OpenAiStreamClient openAiStreamClient;

    public Flux<String> getChatResponse(ChatCompletionStreamRequest request) {
        return openAiStreamClient.sendChatRequest(request)
                .doOnNext(line -> log.info("raw line: [{}]", line))
                .mapNotNull(this::extractContent);
    }

    private String extractContent(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode content = node.path("choices").path(0).path("delta").path("content");
            return content.isMissingNode() || content.isNull() ? null : content.asText();
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
