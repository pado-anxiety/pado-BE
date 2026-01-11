package com.pado.act.recommend.infrastructure;

import com.pado.act.recommend.ACTRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class AiACTRecommendationRepositoryImpl implements AiACTRecommendationRepository {

    private final ACTRecommendJpaRepository actRecommendJpaRepository;

    @Override
    public void saveAiRecommend(Long userId, ACTRecommendation actRecommendation) {
        actRecommendJpaRepository.save(new AiACTRecommendEntity(userId, LocalDateTime.now(), actRecommendation.getActType()));
    }

    @Override
    public int countTodayAiRecommended(Long userId) {
        return actRecommendJpaRepository.countACTRecommendLogByUserIdAndBetweenTime(userId, LocalDateTime.now().toLocalDate().atStartOfDay(), LocalDateTime.now());
    }
}
