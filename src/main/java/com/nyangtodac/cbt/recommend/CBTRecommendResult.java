package com.nyangtodac.cbt.recommend;

import lombok.Getter;

@Getter
public class CBTRecommendResult {
    private final CBT cbt;
    private final String message;

    public CBTRecommendResult(CBT cbt, String message) {
        this.cbt = cbt;
        this.message = message;
    }
}
