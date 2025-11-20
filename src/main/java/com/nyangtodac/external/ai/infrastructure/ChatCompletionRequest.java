package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ChatCompletionRequest {

    private final String model;
    private final List<Message> messages;
    private final Double temperature;
    private final Integer max_tokens;

    public ChatCompletionRequest(String model, SystemPrompt systemPrompt, List<Message> messages, Double temperature, Integer max_tokens) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", systemPrompt.getSystem()));
        this.messages.addAll(messages);
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    @Getter
    @ToString
    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

