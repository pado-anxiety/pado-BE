package com.pado.chat.application;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPreProcessor {

    private final ConversationSummaryService conversationSummaryService;
    private final ChattingContextService contextService;

    public ChatPreProcessResult preProcess(Long userId, Chatting userChatting) {
        ChattingContext chattingContext = contextService.makeContext(userId, userChatting);
        ChatSummaries summaries = conversationSummaryService.getConversationSummaries(userId, 3);
        return new ChatPreProcessResult(chattingContext, summaries);
    }
}
