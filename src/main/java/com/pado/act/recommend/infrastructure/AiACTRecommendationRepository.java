package com.pado.act.recommend.infrastructure;

import com.pado.act.recommend.ACTRecommendation;

import java.time.ZoneId;

public interface AiACTRecommendationRepository {

    void saveAiRecommend(Long userId, ACTRecommendation actRecommendation);

    int countTodayAiRecommended(Long userId, ZoneId zoneId);

    void deleteAllByUserId(Long userId);
}
