package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.tsid.TsidUtil;
import com.nyangtodac.external.ai.application.OpenAiService;
import com.nyangtodac.external.ai.application.response.OpenAiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nyangtodac.chat.controller.dto.Sender.AI;
import static com.nyangtodac.chat.controller.dto.Sender.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class AIChatService {

    private final OpenAiService openAiService;
    private final ChattingService chattingService;

    public MessageResponse postMessage(Long userId, MessageRequest request) {
        Message userMessage = new Message(request.getMessage(), USER);
        MessageContext messageContext = makeContext(userId, userMessage);

        OpenAiChatResponse chatResponse = openAiService.getChatResponse(messageContext);
        Message reply = new Message(chatResponse.getReply(), AI);

        List<Chatting> messages = new ArrayList<>();
        messages.add(userMessage);
        messages.add(reply);
        chattingService.saveChattings(userId, messages);

        return new MessageResponse(reply.getType(), Sender.valueOf(reply.getSender()), reply.getContent(), TsidUtil.toLocalDateTime(reply.getTsid()));
    }

    private MessageContext makeContext(Long userId, Message userMessage) {
        List<Message> chattings = new ArrayList<>(chattingService.makeContext(userId));
        chattings.add(userMessage);
        return new MessageContext(chattings);
    }
}
