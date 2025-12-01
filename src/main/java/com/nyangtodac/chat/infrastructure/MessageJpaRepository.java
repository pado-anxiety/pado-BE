package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.application.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

    @Query("select new com.nyangtodac.chat.application.Message(m.tsid, m.content, m.sender) from MessageEntity m where m.userId = :userId order by m.tsid desc")
    List<Message> findTopNByUserIdOrderByTsidDesc(@Param("userId") Long userId, Pageable pageable);
}
