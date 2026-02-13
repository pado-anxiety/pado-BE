package com.pado.fcm.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmRepository extends JpaRepository<FcmEntity, Long> {
    Optional<FcmEntity> findByUserId(Long userId);
}
