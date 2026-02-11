package com.pado.external.ai.infrastructure;

import com.pado.chat.controller.dto.Sender;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatCompletionMessageConverter {
    
    public List<ChatCompletionRequest.Message> convert(List<Chatting> chattings) {
        return chattings.stream().map(this::toMessage).toList();
    }

    public List<ChatCompletionRequest.Message> convert(ChattingContext context) {
        return convert(context.getChattings());
    }

    private ChatCompletionRequest.Message toMessage(Chatting chatting) {
        String role = Sender.valueOf(chatting.getSender().toUpperCase()).getRole();
        return new ChatCompletionRequest.Message(role, chatting.getMessage());
    }
}