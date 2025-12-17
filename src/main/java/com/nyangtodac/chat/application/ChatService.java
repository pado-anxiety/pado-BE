package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final AIQuotaService aiQuotaService;
    private final AIChatService aiChatService;

    public MessageResponse postMessage(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME

        return aiChatService.postMessage(userId, messageRequest);
    }

    public QuotaStatus getQuotaStatus(Long userId) {
        return aiQuotaService.getQuotaStatus(userId);
    }
}
