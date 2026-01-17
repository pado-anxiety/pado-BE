package com.pado.chat.application;

import com.pado.chat.controller.dto.ChattingResponse;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.chat.quota.QuotaStatus;
import com.pado.external.rabbitmq.ChattingFlushProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChatFacade {

    private final AIChatService aiChatService;
    private final ConversationSummaryService conversationSummaryService;
    private final ChattingContextService contextService;
    private final ChattingFlushProducer chattingFlushProducer;

    public ChattingResponse postMessage(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChattingContext chattingContext = contextService.makeContext(userId, userChatting);
        ChatSummaries summaries = conversationSummaryService.getConversationSummaries(userId, 3);
        Chatting reply = aiChatService.postMessage(chattingContext, summaries);
        List<Chatting> userAndAiChatting = List.of(userChatting, reply);
        contextService.appendContext(userId, userAndAiChatting);
        chattingFlushProducer.publish(userId, userAndAiChatting);
        conversationSummaryService.asyncSummarize(userId);
        return new ChattingResponse(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }
}
