package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.external.ai.application.OpenAiService;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nyangtodac.chat.controller.dto.Sender.AI;
import static com.nyangtodac.chat.controller.dto.Sender.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class AIChatService {

    private final OpenAiService openAiService;
    private final MessageService messageService;

    public MessageResponse postMessage(Long userId, MessageRequest request) {
        MessageContext messageContext = makeContext(userId, request.getContent());

        OpenAiChatResponse chatResponse = openAiService.getChatResponse(messageContext);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(request.getContent(), USER.name()));
        chatResponse.getReplies().forEach(m -> messages.add(new Message(m, AI.name())));
        messageService.saveMessages(userId, messages);

        return new MessageResponse(chatResponse.getReplies());
    }

    private MessageContext makeContext(Long userId, String userMessage) {
        List<Message> messages = new ArrayList<>(
                messageService.makeContext(userId)
                        .stream()
                        .map(msg -> new Message(msg.getContent(), msg.getRole()))
                        .toList()
        );
        messages.add(new Message(userMessage, "user"));
        return new MessageContext(messages);
    }
}
