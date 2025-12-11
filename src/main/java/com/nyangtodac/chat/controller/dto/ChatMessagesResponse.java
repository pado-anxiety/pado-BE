package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChatMessagesResponse {

    private final List<Message> messages;

    public ChatMessagesResponse(List<com.nyangtodac.chat.application.Message> messages) {
        this.messages = messages.stream().map(m -> new Message(Sender.valueOf(m.getSender()), m.getContent(), TsidUtil.toLocalDateTime(m.getTsid()))).toList();
    }

    @Getter
    private static class Message {
        private final Sender sender;
        private final String content;
        private final LocalDateTime time;

        public Message(Sender sender, String content, LocalDateTime time) {
            this.sender = sender;
            this.content = content;
            this.time = time;
        }
    }
}
