package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages;
    private Double temperature;
    private Integer max_tokens;

    public ChatCompletionRequest(String model, SystemPrompt systemPrompt, List<Message> messages, Double temperature, Integer max_tokens) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", systemPrompt.getSystem()));
        this.messages.addAll(messages);
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    public ChatCompletionRequest() {
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

