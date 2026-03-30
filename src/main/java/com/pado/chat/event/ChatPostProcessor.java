package com.pado.chat.event;

import com.pado.chat.application.ChattingContextService;
import com.pado.chat.application.ChattingEncryptService;
import com.pado.chat.application.ConversationSummaryService;
import com.pado.chat.domain.Chatting;
import com.pado.chat.mq.ChattingFlushProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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

    @EventListener
    @Async("chattingExecutor")
    public void postProcess(ChattingEvent event) {
        List<Chatting> encrypted = Stream.of(event.getUserChatting(), event.getReply()).map(encryptService::encrypt).toList();
        contextService.appendContext(event.getUserId(), encrypted);
        chattingFlushProducer.publish(event.getUserId(), encrypted);
        conversationSummaryService.summarize(event.getUserId());
    }
}
