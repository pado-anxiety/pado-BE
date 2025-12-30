package com.nyangtodac.chat.application;

import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.ChattingContext;
import com.nyangtodac.external.ai.application.OpenAiService;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nyangtodac.chat.controller.dto.Sender.AI;

@Service
@Transactional
@RequiredArgsConstructor
public class AIChatService {

    private final OpenAiService openAiService;

    public Chatting postMessage(ChattingContext chattingContext, ChatSummaries summaries) {
        OpenAiChatResponse chatResponse = openAiService.getChatResponse(chattingContext, summaries);
        return new Chatting(chatResponse.getReply(), AI);
    }
}
