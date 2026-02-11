package com.pado.act.recommend.infrastructure;

import com.pado.act.recommend.ACTRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class AiACTRecommendationRepositoryImpl implements AiACTRecommendationRepository {

    private final ACTRecommendJpaRepository actRecommendJpaRepository;

    @Override
    public void saveAiRecommend(Long userId, ACTRecommendation actRecommendation) {
        actRecommendJpaRepository.save(new AiACTRecommendEntity(userId, Instant.now(), actRecommendation.getActType()));
    }

    @Override
    public int countTodayAiRecommended(Long userId, ZoneId zoneId) {
        Instant startOfDay = ZonedDateTime.now(zoneId).toLocalDate().atStartOfDay(zoneId).toInstant();
        return actRecommendJpaRepository.countACTRecommendLogByUserIdAndBetweenTime(userId, startOfDay, ZonedDateTime.now(zoneId).toInstant());
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        actRecommendJpaRepository.deleteAllByUserId(userId);
    }
}
