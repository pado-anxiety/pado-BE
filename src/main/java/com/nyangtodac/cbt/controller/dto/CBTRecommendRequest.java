package com.nyangtodac.cbt.controller.dto;

import com.nyangtodac.cbt.recommend.Situation;
import com.nyangtodac.cbt.recommend.Symptom;
import lombok.Getter;

@Getter
public class CBTRecommendRequest {

    private final Symptom symptom;
    private final int intensity;
    private final Situation situation;

    public CBTRecommendRequest(Symptom symptom, int intensity, Situation situation) {
        this.symptom = symptom;
        this.intensity = intensity;
        this.situation = situation;
    }
}
