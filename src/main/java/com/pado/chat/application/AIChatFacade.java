package com.pado.chat.application;

import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.controller.dto.Sender;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIChatFacade {

    private final ChatPreProcessor chatPreProcessor;
    private final ChatPostProcessor chatPostProcessor;
    private final AIChatService aiChatService;
    private final AIChatStreamingService aiChatStreamingService;

    public PostMessageResult postMessageV1(Long userId, MessageRequest messageRequest) {
//        if (!aiQuotaService.tryConsume(userId)) {
//            QuotaStatus quotaStatus = aiQuotaService.getQuotaStatus(userId);
//            throw new ChatQuotaExceededException(quotaStatus);
//        } FIXME
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChatPreProcessResult preProcessResult = chatPreProcessor.preProcess(userId, userChatting);
        Chatting reply = aiChatService.postMessage(preProcessResult);
        chatPostProcessor.postProcess(userId, userChatting, reply);
        return new PostMessageResult(Sender.valueOf(reply.getSender()), reply.getMessage(), reply.getTsid());
    }

    public Flux<String> postMessageV2(Long userId, MessageRequest messageRequest) {
        Chatting userChatting = new Chatting(messageRequest.getMessage(), Sender.USER);
        ChatPreProcessResult preProcessResult = chatPreProcessor.preProcess(userId, userChatting);

        StringBuilder fullResponse = new StringBuilder();

        return aiChatStreamingService.postMessage(preProcessResult)
                .doOnNext(fullResponse::append)
                .doOnComplete(() ->
                        Mono.fromRunnable(() -> {
                                    Chatting reply = new Chatting(fullResponse.toString(), Sender.AI);
                                    chatPostProcessor.postProcess(userId, userChatting, reply);
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe(
                                        null,
                                        e -> log.error("후처리 실패 userId={}", userId, e)
                                )
                );
    }
}
