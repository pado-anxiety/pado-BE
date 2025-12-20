package com.nyangtodac.chat.controller.dto;

import com.nyangtodac.chat.domain.CBTRecommendation;
import com.nyangtodac.chat.domain.Type;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CBTRecommendationResponse extends ChattingResponse {

    private final CBTRecommendation.Options options;

    public CBTRecommendationResponse(Type type, CBTRecommendation.Options options, LocalDateTime time) {
        super(type, time);
        this.options = options;
    }

}
