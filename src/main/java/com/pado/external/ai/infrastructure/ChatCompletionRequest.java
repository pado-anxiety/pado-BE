package com.pado.external.ai.infrastructure;

import com.pado.external.ai.infrastructure.prompt.SystemPrompt;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages;
    private Double temperature;
    private Integer max_tokens;

    public static ChatCompletionRequest toChatRequest(String model, SystemPrompt systemPrompt, List<Message> summaries, List<Message> messages, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(new Message("system", systemPrompt.getSystem()));
        request.messages.addAll(summaries);
        request.messages.addAll(messages);
        request.temperature = temperature;
        request.max_tokens = max_tokens;
        return request;
    }

    public static ChatCompletionRequest toSummaryRequest(String model, SystemPrompt systemPrompt, List<Message> messages, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(new Message("system", systemPrompt.getSystem()));
        request.messages.addAll(messages);
        request.temperature = temperature;
        request.max_tokens = max_tokens;
        return request;
    }

    public static ChatCompletionRequest toActRecommendRequest(String model, SystemPrompt systemPrompt, List<Message> summary, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(new Message("system", systemPrompt.getSystem()));
        request.messages.addAll(summary);
        request.temperature = temperature;
        request.max_tokens = max_tokens;
        return request;
    }

    @Getter
    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

