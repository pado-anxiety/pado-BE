package com.pado.chat.application;

import com.pado.chat.domain.Chatting;
import com.pado.external.ai.application.OpenAiService;
import com.pado.external.ai.application.response.OpenAiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pado.chat.controller.dto.Sender.AI;

@Service
@Transactional
@RequiredArgsConstructor
public class AIChatService {

    private final OpenAiService openAiService;

    public Chatting postMessage(ChatPreProcessResult preProcessResult) {
        OpenAiChatResponse chatResponse = openAiService.getChatResponse(preProcessResult.getChattingContext(), preProcessResult.getChatSummaries());
        return new Chatting(chatResponse.getReply(), AI);
    }
}
