package com.pado.chat.controller.mapper;

import com.pado.chat.controller.dto.ChattingResponse;
import com.pado.chat.controller.dto.Sender;
import com.pado.util.tsid.ChattingTsidUtil;

import java.time.ZoneId;

public final class ChattingResponseMapper {

    public static ChattingResponse from(Sender sender, String message, Long tsid, ZoneId zoneId) {
        return new ChattingResponse(sender, message, ChattingTsidUtil.toLocalDateTime(tsid, zoneId));
    }
}
