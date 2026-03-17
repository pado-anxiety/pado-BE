package com.pado.diary.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    List<DiaryEntity> findByUserIdAndCreatedAtBetween(Long userId, Instant from, Instant to);
}
