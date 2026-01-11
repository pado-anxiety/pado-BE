package com.nyangtodac.chat;

import com.nyangtodac.chat.application.ChattingContextService;
import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.ChattingContext;
import com.nyangtodac.chat.infrastructure.ChattingDBRepository;
import com.nyangtodac.chat.infrastructure.RecentChattingRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChattingContextServiceTest {

    @Mock
    RecentChattingRedisRepository recentChattingRedisRepository;

    @Mock
    ChattingDBRepository chattingDBRepository;

    ChattingContextService chattingContextService;

    @Test
    @DisplayName("cache hit - 최근 채팅이 contextSize와 같을 경우 db 조회 없이 그대로 context를 생성한다")
    void makeContext_cacheHit() {
        this.chattingContextService = new ChattingContextService(recentChattingRedisRepository, chattingDBRepository, 3);
        //given
        Long userId = 1L;
        Chatting userChatting = new Chatting(10L, "abc", Sender.USER);

        List<Chatting> cached = List.of(
                new Chatting(7L, "abcd", Sender.USER),
                new Chatting(8L, "abce", Sender.USER),
                new Chatting(9L, "abcf", Sender.USER)
        );

        when(recentChattingRedisRepository.getRecentChattings(userId))
                .thenReturn(new ArrayList<>(cached));

        //when
        ChattingContext context = chattingContextService.makeContext(userId, userChatting);

        //then
        assertThat(context.getChattings())
                .extracting(Chatting::getTsid)
                .containsExactly(7L, 8L, 9L, 10L); //원소 값, 순서 검증
        verify(chattingDBRepository, never()).findRecentMessages(eq(userId), anyInt());
    }

    @Test
    @DisplayName("cache miss - 최근 채팅이 contextSize보다 적을 경우 db 조회를 통해 context를 채워준다")
    void makeContext_cacheMiss() {
        this.chattingContextService = new ChattingContextService(recentChattingRedisRepository, chattingDBRepository, 10);
        // given
        Long userId = 1L;
        Chatting userChatting = new Chatting(10L, "abc", Sender.USER);

        List<Chatting> cached = List.of(new Chatting(9L, "abc", Sender.USER));
        List<Chatting> fromDb = List.of(
                new Chatting(6L, "abc", Sender.USER),
                new Chatting(7L, "abc", Sender.USER),
                new Chatting(8L, "abc", Sender.USER)
        );

        when(recentChattingRedisRepository.getRecentChattings(userId))
                .thenReturn(new ArrayList<>(cached));

        when(chattingDBRepository.findRecentMessages(userId, 9))
                .thenReturn(fromDb);

        // when
        ChattingContext context = chattingContextService.makeContext(userId, userChatting);

        // then
        assertThat(context.getChattings())
                .extracting(Chatting::getTsid)
                .containsExactly(6L, 7L, 8L, 9L, 10L);
    }
}
