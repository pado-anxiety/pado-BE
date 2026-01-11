package com.pado.act.recommend.infrastructure;

import com.pado.act.recommend.ACTRecommendation;

public interface AiACTRecommendationRepository {

    void saveAiRecommend(Long userId, ACTRecommendation actRecommendation);

    int countTodayAiRecommended(Long userId);
}
