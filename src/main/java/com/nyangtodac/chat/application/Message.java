package com.nyangtodac.chat.application;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.tsid.TsidUtil;
import lombok.Getter;

@Getter
public class Message {

    private Long tsid;
    private String content;
    private String sender; //AI, USER

    public Message(String content, Sender sender) {
        this.tsid = TsidUtil.generate();
        this.content = content;
        this.sender = sender.name();
    }

    public Message(Long tsid, String content, Sender sender) {
        this.tsid = tsid;
        this.content = content;
        this.sender = sender.name();
    }

    public Message() {
    }
}