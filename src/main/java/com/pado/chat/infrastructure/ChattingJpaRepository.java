package com.pado.chat.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingJpaRepository extends JpaRepository<ChattingEntity, Long> {

    List<ChattingEntity> findByUserIdOrderByTsidDesc(Long userId, Pageable pageable);

    List<ChattingEntity> findByUserIdAndTsidLessThanOrderByTsidDesc(Long userId, Long cursor, Pageable pageable);

    List<ChattingEntity> findByUserIdAndTsidGreaterThanOrderByTsidAsc(Long userId, Long tsid, Pageable pageable);

    void deleteAllByUserId(Long userId);

    @Modifying
    @Query(value = "insert into chatting (tsid, user_id, message, sender) " +
            "values (:#{#chatting.tsid}, :#{#chatting.userId}, :#{#chatting.message}, :#{#chatting.sender.name()}) on conflict(tsid) do nothing", nativeQuery = true)
    void upsert(@Param("chatting") ChattingEntity chatting);
}
