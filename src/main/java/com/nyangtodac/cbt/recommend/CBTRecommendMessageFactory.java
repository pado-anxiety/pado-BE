package com.nyangtodac.cbt.recommend;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CBTRecommendMessageFactory {

    public String makeMessage(Set<CBT> cooldownCBTs, CBTRecommendRequest request) {
        return makeCooldownMessage(cooldownCBTs) + "\n" +
                request.getSymptom().getRandomMessage(request.getIntensity()) + "\n" +
                request.getSituation().getRandomMessage();
    }

    private String makeCooldownMessage(Set<CBT> cooldownCBTs) {
        int cooldownCount = cooldownCBTs.size();
        int totalCount = CBT.values().length;

        return CooldownState.from(cooldownCount, totalCount).message(cooldownCBTs);
    }
}