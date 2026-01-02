package com.nyangtodac.act.recommend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ACTRecommendJpaRepository extends JpaRepository<AiACTRecommendEntity, Long> {

    @Query("select count(*) from AiACTRecommendEntity r where r.userId=:userId and r.time between :from and :to")
    int countACTRecommendLogByUserIdAndBetweenTime(Long userId, LocalDateTime from, LocalDateTime to);
}
