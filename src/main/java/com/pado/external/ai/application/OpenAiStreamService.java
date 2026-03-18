package com.pado.external.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.external.ai.infrastructure.ChatCompletionRequest;
import com.pado.external.ai.infrastructure.OpenAiStreamClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class OpenAiStreamService {

    private final ObjectMapper objectMapper;
    private final OpenAiStreamClient openAiStreamClient;

    public Flux<String> getChatResponse(ChatCompletionRequest chatCompletionRequest) {
        return openAiStreamClient.sendChatRequest(chatCompletionRequest)
                .filter(line -> !line.isBlank())
                .filter(line -> !line.equals("data: [DONE]"))
                .map(line -> line.startsWith("data: ") ? line.substring(6) : line)
                .mapNotNull(this::extractContent);
    }

    private String extractContent(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode content = node
                    .path("choices")
                    .path(0)
                    .path("delta")
                    .path("content");

            return content.isMissingNode() || content.isNull()
                    ? null
                    : content.asText();
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
