package com.pado.chat.event;

import com.pado.chat.application.ConversationSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPostProcessor {

    private final ConversationSummaryService conversationSummaryService;

    @EventListener
    @Async("chattingExecutor")
    public void postProcess(ChattingEvent event) {
        conversationSummaryService.summarize(event.getUserId());
    }
}
