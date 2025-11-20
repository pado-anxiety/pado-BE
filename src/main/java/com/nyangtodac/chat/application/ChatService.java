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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiService openAiService;
    private final MessageRepository messageRepository;

    public MessageResponse postMessage(Long userId, MessageRequest request) {
        RecentMessages recentMessages = getRecentMessages(userId);

        OpenAiChatResponse chatResponse = openAiService.getChatResponse(recentMessages, request.getContent());

        MessageEntity userMessage = new MessageEntity(userId, Sender.USER, request.getContent());
        messageRepository.save(userMessage);

        List<MessageEntity> aiMessages = chatResponse.getReplies().stream().map(m -> new MessageEntity(userId, Sender.AI, m)).toList();
        messageRepository.saveAll(aiMessages);

        return new MessageResponse(chatResponse.getReplies());
    }

    private RecentMessages getRecentMessages(Long userId) {
        List<String> context = messageRepository
                .findTop10ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(msg -> {
                    if (msg.getSender() == Sender.AI) {
                        return "[CONTEXT - AI]: " + msg.getContent();
                    } else {
                        return "[CONTEXT - USER]: " + msg.getContent();
                    }
                })
                .collect(Collectors.toList());
        Collections.reverse(context);
        return new RecentMessages(context);
    }
}
