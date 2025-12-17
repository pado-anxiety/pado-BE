package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.CBT;
import lombok.Getter;

import java.util.List;

@Getter
public class CBTRecommendResponse {
    private final CBT cbt;
    private final List<String> messages;

    public CBTRecommendResponse(CBT cbt, List<String> messages) {
        this.cbt = cbt;
        this.messages = messages;
    }
}
