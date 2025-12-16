package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.CBT;
import lombok.Getter;

@Getter
public class CBTRecommendResponse {
    private final CBT cbt;

    public CBTRecommendResponse(CBT cbt) {
        this.cbt = cbt;
    }
}
