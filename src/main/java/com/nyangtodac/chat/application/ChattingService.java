package com.nyangtodac.chat.application;

import com.nyangtodac.chat.domain.RecentChattings;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.ChattingContext;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import com.nyangtodac.chat.infrastructure.ChattingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {

    private static final int CONTEXT_SIZE = 10;
    private static final int CHATTING_PAGINATION_SIZE = 30;

    private final ChattingRedisRepository chattingRedisRepository;
    private final ChattingDBRepository chattingDBRepository;

    public void saveChattings(Long userId, List<Chatting> chattings) {
        List<Chatting> overflowChattings = chattingRedisRepository.saveMessages(userId, chattings);
        if (!overflowChattings.isEmpty()) {
            chattingDBRepository.asyncBatch(userId, overflowChattings).exceptionally(
                    e -> {
                        log.error("Failed to flush [overflow messages] for userId: {}", userId, e);
                        chattingRedisRepository.saveFlushFailedMessages(userId, chattings);
                        return null;
                    }
            );
        }
    }

    @Transactional(readOnly = true)
    public RecentChattings getRecentChattingsBeforeCursor(Long userId, Long cursor) {
        if (cursor == null) {
            cursor = Long.MAX_VALUE;
        }
        List<Chatting> chattings = new ArrayList<>(chattingRedisRepository.findRecentChattingsLessThanCursor(userId, cursor, CHATTING_PAGINATION_SIZE));
        if (chattings.size() < CHATTING_PAGINATION_SIZE) {
            int left = CHATTING_PAGINATION_SIZE - chattings.size();
            List<Chatting> dbMessages = chattingDBRepository.findRecentChattingsLessThanCursor(userId, cursor, left);
            chattings.addAll(dbMessages);
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

    @Transactional(readOnly = true)
    public ChattingContext makeContext(Long userId, Chatting userChatting) {
        List<Chatting> chattings = new ArrayList<>(chattingRedisRepository.findRecentChattings(userId, CONTEXT_SIZE));
        int left = CONTEXT_SIZE - chattings.size();

        if (left > 0) {
            List<Chatting> dbChattings = new ArrayList<>(chattingDBRepository.findRecentMessages(userId, left));
            Collections.reverse(dbChattings);
            chattings.addAll(0, dbChattings);
        }
        chattings.add(userChatting);

        return new ChattingContext(chattings);
    }
}
