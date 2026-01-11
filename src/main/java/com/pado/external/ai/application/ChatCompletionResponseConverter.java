package com.pado.external.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.external.ai.application.response.OpenAiActRecommendationResponse;
import com.pado.external.ai.application.response.OpenAiChatResponse;
import com.pado.external.ai.application.response.OpenAiSummaryResponse;
import com.pado.external.ai.infrastructure.ChatCompletionResponse;
import com.pado.external.ai.resilience4j.retry.OpenAiClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatCompletionResponseConverter {

    private final ObjectMapper objectMapper;

    public OpenAiChatResponse convertToChat(ChatCompletionResponse response) {
        String content = getContentFrom(response);
        return new OpenAiChatResponse(content);
    }

    public OpenAiSummaryResponse convertToSummary(ChatCompletionResponse response) {
        String content = getContentFrom(response);
        return new OpenAiSummaryResponse(content);
    }

    public Optional<OpenAiActRecommendationResponse> convertToACTRecommendation(ChatCompletionResponse response) {
        String rawJson = getContentFrom(response);
        try {
            return Optional.of(objectMapper.readValue(rawJson, OpenAiActRecommendationResponse.class));
        } catch (JsonProcessingException e) {
            log.warn("OpenAi ACT Recommendation Parsing failed. json={}, error={}", rawJson, e.getMessage());
            return Optional.empty();
        }
    }

    private String getContentFrom(ChatCompletionResponse response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()) {
            throw new OpenAiClientException("OpenAI 응답이 비어있습니다.");
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}
