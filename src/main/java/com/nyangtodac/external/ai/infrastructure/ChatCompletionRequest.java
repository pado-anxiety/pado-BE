package com.nyangtodac.external.ai.infrastructure;

import com.nyangtodac.external.ai.infrastructure.prompt.Prompt;
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

    public ChatCompletionRequest(String model, Prompt prompt, Double temperature, Integer max_tokens) {
        this.model = model;
        this.messages = new ArrayList<>();
        messages.add(new Message("system", prompt.getSystem()));
        messages.add(new Message("user", prompt.getUser()));
        messages.add(new Message("assistant", prompt.getAssistant()));
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

