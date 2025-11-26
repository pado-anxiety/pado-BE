package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.application.Message;

import java.util.List;

public interface MessageDbRepository {
    List<Message> findTopNByUserIdOrderByCreatedAtDescIdDesc(Long userId, int n);

    void asyncFlush(Long userId, List<Message> messages);

}
