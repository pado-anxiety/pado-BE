package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.chat.mq.ChattingFlushProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ChatPostProcessor {

    private final ConversationSummaryService conversationSummaryService;
    private final ChattingContextService contextService;
    private final ChattingFlushProducer chattingFlushProducer;
    private final ChattingEncryptService encryptService;

    public void postProcess(Long userId, Chatting userChatting, Chatting reply) {
        List<Chatting> encrypted = Stream.of(userChatting, reply).map(encryptService::encrypt).toList(); // 후처리 작업
        boolean published = chattingFlushProducer.publish(userId, encrypted);
        if (published) {
            contextService.appendContext(userId, encrypted);
        }
        conversationSummaryService.asyncSummarize(userId);
    }
}
