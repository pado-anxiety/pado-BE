package com.nyangtodac.chat.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.Getter;

@Getter
public class Message extends Chatting {

    private final String content;
    private final String sender;

    public Message(String content, Sender sender) {
        super(Type.CHAT, TsidUtil.generate());
        this.content = content;
        this.sender = sender.name();
    }

    @JsonCreator
    public Message(
            @JsonProperty("tsid") Long tsid,
            @JsonProperty("content") String content,
            @JsonProperty("sender") Sender sender) {
        super(Type.CHAT, tsid);
        this.content = content;
        this.sender = sender.name();
    }

}