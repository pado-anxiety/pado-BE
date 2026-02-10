package com.pado.chat.application.command;

import com.pado.chat.controller.dto.Sender;
import lombok.Getter;

@Getter
public class PostMessageResult {

    private final Sender sender;
    private final String message;
    private final Long tsid;

    public PostMessageResult(Sender sender, String message, Long tsid) {
        this.sender = sender;
        this.message = message;
        this.tsid = tsid;
    }
}
