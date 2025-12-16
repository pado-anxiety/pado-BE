package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.CBT;
import lombok.Getter;

@Getter
public class CBTRecommendResponse {
    private final CBT cbt;
    private final String message;

    public CBTRecommendResponse(CBT cbt, String message) {
        this.cbt = cbt;
        this.message = message;
    }
}
