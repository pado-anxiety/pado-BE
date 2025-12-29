package com.nyangtodac.external.ai.application.response;

import com.nyangtodac.act.recommend.ACT;
import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiActRecommendationResponse {
    private ACT act;
    private List<String> reasons;

    public OpenAiActRecommendationResponse(ACT act, List<String> reasons) {
        this.act = act;
        this.reasons = reasons;
    }
}
