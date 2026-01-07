package com.nyangtodac.chat.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.Getter;

@Getter
public class Chatting {

    private final Long tsid;
    private final String message;
    private final String sender;

    public Chatting(String message, Sender sender) {
        this.tsid = TsidUtil.generate();
        this.message = message;
        this.sender = sender.name();
    }

    @JsonCreator
    public Chatting(
            @JsonProperty("tsid") Long tsid,
            @JsonProperty("message") String message,
            @JsonProperty("sender") Sender sender) {
        this.tsid = tsid;
        this.message = message;
        this.sender = sender.name();
    }

}