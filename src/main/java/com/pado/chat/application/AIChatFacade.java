package com.pado.chat.application;

import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.chat.quota.ChatQuotaExceededException;
import com.pado.chat.quota.QuotaStatus;
import com.pado.external.rabbitmq.ChattingFlushProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AIChatFacade {

    private final AIChatService aiChatService;
    private final AIChatQuotaService aiChatQuotaService;
    private final ConversationSummaryService conversationSummaryService;
    private final ChattingContextService contextService;
    private final ChattingFlushProducer chattingFlushProducer;
    private final ChattingEncryptService encryptService;

    public PostMessageResult postMessage(Long userId, MessageRequest messageRequest) {
        if (!aiChatQuotaService.tryConsume(userId)) {
            QuotaStatus quotaStatus = aiChatQuotaService.getQuotaStatus(userId);
            throw new ChatQuotaExceededException(quotaStatus);
        }
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChattingContext chattingContext = contextService.makeContext(userId, userChatting);
        ChatSummaries summaries = conversationSummaryService.getConversationSummaries(userId, 3);
        Chatting reply = aiChatService.postMessage(chattingContext, summaries);
        List<Chatting> encrypted = Stream.of(userChatting, reply).map(encryptService::encrypt).toList();
        contextService.appendContext(userId, encrypted);
        chattingFlushProducer.publish(userId, encrypted);
        conversationSummaryService.asyncSummarize(userId);
        return new PostMessageResult(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }
}
