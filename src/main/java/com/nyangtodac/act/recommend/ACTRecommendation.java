package com.nyangtodac.act.recommend;

import lombok.Getter;

import java.util.List;

@Getter
public class ACTRecommendation {

    private final ACT act;
    private final List<String> reasons;

    public ACTRecommendation(ACT act, List<String> reasons) {
        this.act = act;
        this.reasons = reasons;
    }
}
