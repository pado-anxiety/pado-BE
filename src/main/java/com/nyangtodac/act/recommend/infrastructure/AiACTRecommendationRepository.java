package com.nyangtodac.act.recommend.infrastructure;

import com.nyangtodac.act.recommend.ACTRecommendation;

public interface AiACTRecommendationRepository {

    void saveAiRecommend(Long userId, ACTRecommendation actRecommendation);

    int countTodayAiRecommended(Long userId);
}
