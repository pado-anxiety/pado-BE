package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.tsid.TsidUtil;
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
        this.time = TsidUtil.toLocalDateTime(tsid);
    }
}
