package com.nyangtodac.chat.application;

import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.RecentChattings;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import com.nyangtodac.chat.infrastructure.RecentChattingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {

    private static final int CHATTING_PAGINATION_SIZE = 30;

    private final RecentChattingRedisRepository recentChattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    @Transactional(readOnly = true)
    public RecentChattings getRecentChattingsBeforeCursor(Long userId, Long cursor) {
        List<Chatting> chattings;
        if (cursor == null) {
            chattings = new ArrayList<>(recentChattingRedisRepository.getRecentChattings(userId));
            if (chattings.isEmpty()) {
                chattings = new ArrayList<>(chattingDBRepository.findRecentChattingsLessThanCursor(userId, Long.MAX_VALUE, CHATTING_PAGINATION_SIZE));
            }
        } else {
            chattings = new ArrayList<>(chattingDBRepository.findRecentChattingsLessThanCursor(userId, cursor, CHATTING_PAGINATION_SIZE));
        }
        chattings.sort(Comparator.comparing(Chatting::getTsid).reversed()); //0번째 채팅이 가장 최근 채팅
        Long nextCursor = null;
        if (!chattings.isEmpty()) {
            nextCursor = chattings.get(chattings.size() - 1).getTsid();
        }
        return new RecentChattings(chattings, nextCursor);
    }

    @Transactional(readOnly = true)
    public List<Chatting> getRecentChattingsAfterCursorOrderByTsidAscFromDB(Long userId, Long cursor, int limit) {
        return chattingDBRepository.findChattingsAfterTsidOrderByTsidAsc(userId, cursor, limit);
    }
}
