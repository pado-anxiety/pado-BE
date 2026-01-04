package com.nyangtodac.act.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ACTRecordRepository extends JpaRepository<ACTRecordEntity, Long> {

    List<ACTRecordEntity> findAllByUserIdOrderByTimeDesc(Long userId);
}
