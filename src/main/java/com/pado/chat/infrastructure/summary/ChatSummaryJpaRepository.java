package com.pado.chat.infrastructure.summary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSummaryJpaRepository extends JpaRepository<ChatSummaryEntity, Long> {
    List<ChatSummaryEntity> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}
