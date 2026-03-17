package com.pado.chat.application;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChattingContext;
import lombok.Getter;

@Getter
public class ChatPreProcessResult {

    private final ChattingContext chattingContext;
    private final ChatSummaries chatSummaries;

    public ChatPreProcessResult(ChattingContext chattingContext, ChatSummaries chatSummaries) {
        this.chattingContext = chattingContext;
        this.chatSummaries = chatSummaries;
    }
}
