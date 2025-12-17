package com.nyangtodac.chat.controller.dto.mapper;

import com.nyangtodac.chat.application.CBTRecommendation;
import com.nyangtodac.chat.application.Chatting;
import com.nyangtodac.chat.application.Message;
import com.nyangtodac.chat.application.Type;
import com.nyangtodac.chat.controller.dto.CBTRecommendationResponse;
import com.nyangtodac.chat.controller.dto.ChattingResponse;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.tsid.TsidUtil;

import java.time.LocalDateTime;

public final class ChattingResponseMapper {

    private ChattingResponseMapper() {}

    public static ChattingResponse from(Chatting chatting) {

        LocalDateTime time = TsidUtil.toLocalDateTime(chatting.getTsid());
        Type type = chatting.getType();

        return switch (type) {

            case CHAT -> {
                Message message = (Message) chatting;
                yield new MessageResponse(
                        Type.CHAT,
                        Sender.valueOf(message.getSender()),
                        message.getContent(),
                        time
                );
            }

            case CBT_RECOMMENDATION -> {
                CBTRecommendation cbt = (CBTRecommendation) chatting;
                yield new CBTRecommendationResponse(
                        Type.CBT_RECOMMENDATION,
                        cbt.getOptions(),
                        time
                );
            }
        };
    }
}
