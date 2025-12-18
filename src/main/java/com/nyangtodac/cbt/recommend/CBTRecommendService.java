package com.nyangtodac.cbt.recommend;

import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CBTRecommendService {

    private final CBTRecommender recommender;
    private final CBTHistoryRedisRepository historyRepository;
    private final CBTRecommendMessageFactory recommendMessageFactory;

    public CBTRecommendResult recommend(Long userId, CBTRecommendRequest request) {
        Set<CBT> cooldownCBTs = historyRepository.getCooldownCBTs(userId);
        boolean allInCooldown = cooldownCBTs.size() == CBT.values().length;

        CBT recommendedCBT = recommender.recommend(
                request.getSymptom(),
                request.getIntensity(),
                request.getSituation(),
                allInCooldown ? Collections.emptySet() : cooldownCBTs
        );

        historyRepository.setCooldown(userId, recommendedCBT);

        return new CBTRecommendResult(recommendedCBT, recommendMessageFactory.makeMessage(cooldownCBTs, request));
    }

}
