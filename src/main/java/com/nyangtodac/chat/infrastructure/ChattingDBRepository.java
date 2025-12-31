package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.domain.Chatting;

import java.util.List;

public interface ChattingDBRepository {

    Chatting save(Long userId, Chatting chatting);

    List<Chatting> findRecentChattingsLessThanCursor(Long userId, Long cursor, int n);

    List<Chatting> findRecentMessages(Long userId, int n);

    List<Chatting> findChattingsAfterTsidOrderByTsidAsc(Long userId, Long latestTsid, int limit);
}
