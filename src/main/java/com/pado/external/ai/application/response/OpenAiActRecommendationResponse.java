package com.pado.external.ai.application.response;

import com.pado.act.ACTType;
import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiActRecommendationResponse {
    private ACTType actType;
    private List<String> reasons;

    public OpenAiActRecommendationResponse(ACTType actType, List<String> reasons) {
        this.actType = actType;
        this.reasons = reasons;
    }
}
