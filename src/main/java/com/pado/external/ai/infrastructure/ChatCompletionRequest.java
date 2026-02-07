package com.pado.external.ai.infrastructure;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();
    private Double temperature;
    private Integer max_tokens;

    private ChatCompletionRequest(Builder builder) {
        this.model = builder.model;
        this.messages.addAll(builder.systemMessages);
        this.messages.addAll(builder.conversationMessages);
        this.temperature = builder.temperature;
        this.max_tokens = builder.maxTokens;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Getter
    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public static Message system(String content) {
            return new Message("system", content);
        }

        public static Message user(String content) {
            return new Message("user", content);
        }

        public static Message assistant(String content) {
            return new Message("assistant", content);
        }
    }

    public static class Builder {
        private String model;
        private final List<Message> systemMessages = new ArrayList<>();
        private final List<Message> conversationMessages = new ArrayList<>();
        private Double temperature;
        private Integer maxTokens;

        public static Builder builder() {
            return new Builder();
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder systemMessage(String content) {
            this.systemMessages.add(Message.system(content));
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.conversationMessages.addAll(messages);
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(this);
        }
    }
}

