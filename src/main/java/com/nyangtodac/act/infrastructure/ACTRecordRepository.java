package com.nyangtodac.act.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ACTRecordRepository extends JpaRepository<ACTRecordEntity, Long> {

    List<ACTRecordEntity> findAllByUserIdAndTsidLessThanOrderByTsidDesc(Long userId, Long cursor, Pageable pageable);
}
