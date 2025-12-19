package com.nyangtodac.chat.application;

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

    public Message postMessage(MessageContext messageContext) {
        OpenAiChatResponse chatResponse = openAiService.getChatResponse(messageContext);
        return new Message(chatResponse.getReply(), AI);
    }
}
