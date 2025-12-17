package com.nyangtodac.cbt.recommend;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CBTRecommendMessageFactory {

    public List<String> makeMessage(Set<CBT> cooldownCBTs, CBTRecommendRequest request) {
        return List.of(makeCooldownMessage(cooldownCBTs), request.getSituation().getRandomMessage());
    }

    private String makeCooldownMessage(Set<CBT> cooldownCBTs) {
        int cooldownCount = cooldownCBTs.size();
        int totalCount = CBT.values().length;

        return CooldownState.from(cooldownCount, totalCount).message(cooldownCBTs);
    }
}