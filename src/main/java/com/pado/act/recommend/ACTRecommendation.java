package com.pado.act.recommend;

import com.pado.act.ACTType;
import lombok.Getter;

import java.util.List;

@Getter
public class ACTRecommendation {

    private final ACTType actType;
    private final List<String> reasons;

    public ACTRecommendation(ACTType actType, List<String> reasons) {
        this.actType = actType;
        this.reasons = reasons;
    }
}
