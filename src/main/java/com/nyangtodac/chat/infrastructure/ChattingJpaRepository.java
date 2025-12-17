package com.nyangtodac.chat.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingJpaRepository extends JpaRepository<ChattingEntity, Long> {

    List<ChattingEntity> findTopNByUserIdOrderByTsidDesc(@Param("userId") Long userId, Pageable pageable);
}
