package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
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

    public ChatMessagesResponse postMessage(Long userId, MessageRequest request) {
        Message userMessage = new Message(request.getContent(), USER);
        MessageContext messageContext = makeContext(userId, request.getContent());

        OpenAiChatResponse chatResponse = openAiService.getChatResponse(messageContext);

        List<Message> messages = new ArrayList<>();
        messages.add(userMessage);
        chatResponse.getReplies().forEach(m -> messages.add(new Message(m, AI)));
        messageService.saveMessages(userId, messages);

        messages.remove(0);
        return new ChatMessagesResponse(messages);
    }

    private MessageContext makeContext(Long userId, String userMessage) {
        List<Message> messages = new ArrayList<>(messageService.makeContext(userId));
        messages.add(new Message(userMessage, USER));
        return new MessageContext(messages);
    }
}
