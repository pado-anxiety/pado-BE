package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.ChattingResponse;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.MessageContext;
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

    public ChattingResponse postMessage(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        MessageContext messageContext = chattingService.makeContext(userId, userChatting);
        Chatting reply = aiChatService.postMessage(messageContext);
        chattingService.saveChattings(userId, List.of(userChatting, reply));
        return new ChattingResponse(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }

    public QuotaStatus getQuotaStatus(Long userId) {
        return aiQuotaService.getQuotaStatus(userId);
    }
}
