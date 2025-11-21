package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.infrastructure.MessageEntity;
import com.nyangtodac.chat.infrastructure.MessageRepository;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import com.nyangtodac.external.ai.application.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiService openAiService;
    private final MessageRepository messageRepository;

    public MessageResponse postMessage(Long userId, MessageRequest request) {
        MessageContext messageContext = makeContext(userId, request.getContent());

        OpenAiChatResponse chatResponse = openAiService.getChatResponse(messageContext);

        MessageEntity userMessage = new MessageEntity(userId, Sender.USER, request.getContent());
        messageRepository.save(userMessage);

        List<MessageEntity> aiMessages = chatResponse.getReplies().stream().map(m -> new MessageEntity(userId, Sender.AI, m)).toList();
        messageRepository.saveAll(aiMessages);

        return new MessageResponse(chatResponse.getReplies());
    }

    private MessageContext makeContext(Long userId, String userMessage) {
        List<MessageContext.Message> messages = new ArrayList<>(messageRepository
                .findTop10ByUserIdOrderByCreatedAtDescIdDesc(userId)
                .stream()
                .map(msg -> new MessageContext.Message(msg.getContent(), msg.getSender().getRole()))
                .toList());
        Collections.reverse(messages);
        messages.add(new MessageContext.Message(userMessage, "user"));
        return new MessageContext(messages);
    }
}
