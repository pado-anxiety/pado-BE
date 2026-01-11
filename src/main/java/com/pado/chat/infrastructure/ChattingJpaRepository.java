package com.pado.chat.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingJpaRepository extends JpaRepository<ChattingEntity, Long> {

    List<ChattingEntity> findByUserIdOrderByTsidDesc(Long userId, Pageable pageable);

    List<ChattingEntity> findByUserIdAndTsidLessThanOrderByTsidDesc(Long userId, Long cursor, Pageable pageable);

    List<ChattingEntity> findByUserIdAndTsidGreaterThanOrderByTsidAsc(Long userId, Long tsid, Pageable pageable);
}
