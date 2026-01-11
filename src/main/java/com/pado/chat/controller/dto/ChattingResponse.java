package com.pado.chat.controller.dto;

import com.pado.tsid.ChattingTsidUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingResponse {
    private final Sender sender;
    private final String message;
    private final LocalDateTime time;

    public ChattingResponse(Sender sender, String message, Long tsid) {
        this.sender = sender;
        this.message = message;
        this.time = ChattingTsidUtil.toLocalDateTime(tsid);
    }
}
