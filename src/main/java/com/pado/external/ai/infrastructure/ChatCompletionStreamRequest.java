package com.pado.external.ai.infrastructure;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatCompletionStreamRequest {

    private final String model;
    private final List<Message> messages;
    private final Double temperature;
    private final Integer max_tokens;
    private final boolean stream = true;

    public ChatCompletionStreamRequest(String model, List<Message> messages, Double temperature, Integer max_tokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
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
