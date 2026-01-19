package com.pado.external.ai.infrastructure;

import com.pado.chat.controller.dto.Sender;
import com.pado.chat.domain.ChatSummary;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages;
    private Double temperature;
    private Integer max_tokens;

    public static ChatCompletionRequest toChatRequest(String model, String systemPrompt, String summaryPrompt, ChattingContext context, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(Message.system(systemPrompt));
        request.messages.add(Message.system(summaryPrompt));
        request.messages.addAll(
                context.getChattings().stream().map(
                        msg -> new ChatCompletionRequest.Message(
                                Sender.valueOf(msg.getSender().toUpperCase()).getRole(),
                                msg.getMessage()
                        )).toList());
        request.temperature = temperature;
        request.max_tokens = max_tokens;
        return request;
    }

    public static ChatCompletionRequest toSummaryRequest(String model, String systemPrompt, List<Chatting> chattings, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(Message.system(systemPrompt));
        request.messages.addAll(
                chattings.stream().map(
                        msg -> new ChatCompletionRequest.Message(
                                Sender.valueOf(msg.getSender().toUpperCase()).getRole(),
                                msg.getMessage()
                        )).toList());
        request.temperature = temperature;
        request.max_tokens = max_tokens;
        return request;
    }

    public static ChatCompletionRequest toActRecommendRequest(String model, String systemPrompt, ChatSummary summary, Double temperature, Integer max_tokens) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.model = model;
        request.messages = new ArrayList<>();
        request.messages.add(Message.system(systemPrompt));
        request.messages.add(new ChatCompletionRequest.Message("user", summary.getSummaryText()));
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

        public static Message system(String content) {
            return new Message("system", content);
        }
    }
}

