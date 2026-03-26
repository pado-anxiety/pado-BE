package com.pado.chat.application;

import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.Chatting;
import com.pado.chat.event.ChattingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIChatFacade {

    private final ChatPreProcessor chatPreProcessor;
    private final AIChatService aiChatService;
    private final ChattingEncryptService encryptService;
    private final ChattingContextService contextService;
    private final ChattingPersistService persistService;

    private final ApplicationEventPublisher eventPublisher;

    public PostMessageResult postMessageV1(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChatPreProcessResult preProcessResult = chatPreProcessor.preProcess(userId, userChatting);
        Chatting reply = aiChatService.postMessage(preProcessResult);
        List<Chatting> encrypted = Stream.of(userChatting, reply).map(encryptService::encrypt).toList();
        persistService.saveAll(userId, encrypted);
        contextService.appendContext(userId, encrypted);
        eventPublisher.publishEvent(new ChattingEvent(userId, userChatting, reply));
        return new PostMessageResult(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }
}
