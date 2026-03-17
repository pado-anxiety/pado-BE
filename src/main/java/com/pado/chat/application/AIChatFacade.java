package com.pado.chat.application;

import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.Chatting;
import com.pado.chat.quota.ChatQuotaExceededException;
import com.pado.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIChatFacade {

    private final ChatPreProcessor chatPreProcessor;
    private final ChatPostProcessor chatPostProcessor;
    private final AIChatService aiChatService;
    private final AIChatQuotaService aiQuotaService;

    public PostMessageResult postMessageV1(Long userId, MessageRequest messageRequest) {
        if (!aiQuotaService.tryConsume(userId)) {
            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
            throw new ChatQuotaExceededException(quotaStatus);
        }
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChatPreProcessResult preProcessResult = chatPreProcessor.preProcess(userId, userChatting);
        Chatting reply = aiChatService.postMessage(preProcessResult);
        chatPostProcessor.postProcess(userId, userChatting, reply);
        return new PostMessageResult(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }
}
