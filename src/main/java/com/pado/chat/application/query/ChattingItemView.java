package com.pado.chat.application.query;

import com.pado.chat.controller.dto.Sender;
import lombok.Getter;

@Getter
public class ChattingItemView {

    private final Long tsid;
    private final String message;
    private final Sender sender;

    public ChattingItemView(Long tsid, String message, Sender sender) {
        this.tsid = tsid;
        this.message = message;
        this.sender = sender;
    }
}
