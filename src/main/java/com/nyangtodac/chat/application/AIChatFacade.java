package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.quota.QuotaStatus;
import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChatFacade {

    private final AIQuotaService aiQuotaService;
    private final AIChatService aiChatService;
    private final ChattingService chattingService;

    public MessageResponse postMessage(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Message userMessage = new Message(messageRequest.getMessage(), Sender.USER);
        MessageContext messageContext = chattingService.makeContext(userId, userMessage);
        Message reply = aiChatService.postMessage(messageContext);
        chattingService.saveChattings(userId, List.of(userMessage, reply));
        return new MessageResponse(reply.getType(), Sender.valueOf(reply.getSender()), reply.getContent(), TsidUtil.toLocalDateTime(reply.getTsid()));
    }

    public QuotaStatus getQuotaStatus(Long userId) {
        return aiQuotaService.getQuotaStatus(userId);
    }
}
