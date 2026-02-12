package com.pado.chat.application;

import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
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
    private final ChattingEncryptService encryptService;

    public PostMessageResult postMessage(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChattingContext chattingContext = contextService.makeContext(userId, userChatting);
        ChatSummaries summaries = conversationSummaryService.getConversationSummaries(userId, 3);
        Chatting reply = aiChatService.postMessage(chattingContext, summaries);
        List<Chatting> chattings = List.of(userChatting, reply);
        contextService.appendContext(userId, chattings);
        chattingFlushProducer.publish(userId, chattings.stream().map(encryptService::encrypt).toList());
        conversationSummaryService.asyncSummarize(userId);
        return new PostMessageResult(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }
}
