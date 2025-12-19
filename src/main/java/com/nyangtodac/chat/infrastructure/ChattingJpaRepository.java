package com.nyangtodac.chat.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingJpaRepository extends JpaRepository<ChattingEntity, Long> {

    List<ChattingEntity> findTopNByUserIdOrderByTsidDesc(Long userId, Pageable pageable);

    List<ChattingEntity> findTopNByUserIdAndTsidLessThanOrderByTsidDesc(Long userId, Long cursor, Pageable pageable);
}
