package com.nyangtodac.cbt.recommend;

import lombok.Getter;

import java.util.List;

@Getter
public class CBTRecommendResult {
    private final CBT cbt;
    private final List<String> messages;

    public CBTRecommendResult(CBT cbt, List<String> messages) {
        this.cbt = cbt;
        this.messages = messages;
    }
}
