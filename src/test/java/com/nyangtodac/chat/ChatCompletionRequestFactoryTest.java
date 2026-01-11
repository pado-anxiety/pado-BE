package com.nyangtodac.chat;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.domain.ChatSummaries;
import com.nyangtodac.chat.domain.ChatSummary;
import com.nyangtodac.chat.domain.Chatting;
import com.nyangtodac.chat.domain.ChattingContext;
import com.nyangtodac.config.properties.ACTRecommendOpenAiProperties;
import com.nyangtodac.config.properties.ChatOpenAiProperties;
import com.nyangtodac.config.properties.ChatSummaryOpenAiProperties;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequest;
import com.nyangtodac.external.ai.infrastructure.ChatCompletionRequestFactory;
import com.nyangtodac.external.ai.infrastructure.prompt.PromptManager;
import com.nyangtodac.external.ai.infrastructure.prompt.SystemPrompt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatCompletionRequestFactoryTest {

    @Mock
    PromptManager promptManager;

    @Mock
    ChatOpenAiProperties chatOpenAiProperties;

    @Mock
    ChatSummaryOpenAiProperties chatSummaryOpenAiProperties;

    @Mock
    ACTRecommendOpenAiProperties actRecommendOpenAiProperties;

    @InjectMocks
    ChatCompletionRequestFactory factory;

    @Test
    @DisplayName("채팅 ChatCompletionRequest 생성 테스트 - 요약 생성 확인 테스트")
    void buildChatRequest_summary가_있을_때_정상적으로_구성된다() {
        // given
        SystemPrompt systemPrompt = new SystemPrompt("시스템");
        when(promptManager.getChatSystemPrompt()).thenReturn(systemPrompt);
        SystemPrompt summaryPrefixPrompt = new SystemPrompt("요약 prefix");
        when(promptManager.getSummaryPrefix()).thenReturn(summaryPrefixPrompt);

        when(chatOpenAiProperties.getModel()).thenReturn("모델");
        when(chatOpenAiProperties.getTemperature()).thenReturn(0.7);
        when(chatOpenAiProperties.getMaxTokens()).thenReturn(1024);

        ChatSummary summary1 = new ChatSummary(1L, 3L, "요약1");
        ChatSummary summary2 = new ChatSummary(3L, 5L, "요약2");

        ChatSummaries summaries = new ChatSummaries(List.of(summary1, summary2));

        Chatting chatting = new Chatting(6L, "안녕", Sender.USER);
        ChattingContext context = new ChattingContext(List.of(chatting));

        // when
        ChatCompletionRequest request = factory.buildChatRequest(context, summaries);

        // then
        assertThat(request.getModel()).isEqualTo("모델");
        assertThat(request.getTemperature()).isEqualTo(0.7);
        assertThat(request.getMax_tokens()).isEqualTo(1024);

        // messages
        List<ChatCompletionRequest.Message> messages = request.getMessages();

        // system 메시지 검증
        ChatCompletionRequest.Message system = messages.get(0);
        assertThat(system.getRole()).isEqualTo("system");
        assertThat(system.getContent()).isEqualTo(
                "시스템"
        );

        //요약 검증
        ChatCompletionRequest.Message summary1Message = messages.get(1);
        System.out.println(summary1Message.getContent());
        assertThat(summary1Message.getRole()).isEqualTo("system");
        assertThat(summary1Message.getContent()).isEqualTo("""
                요약 prefix
                요약1
                
                요약2""");

        //user message 검증
        ChatCompletionRequest.Message userMessage = messages.get(2);
        assertThat(userMessage.getRole()).isEqualTo("user");
        assertThat(userMessage.getContent()).isEqualTo("안녕");
    }

    @Test
    @DisplayName("채팅 ChatCompletionRequest 생성 테스트 - 요약 빈 값 확인 테스트")
    void buildChatRequest_summary가_없으면_summaryMessage는_생성되지_않는다() {
        //given
        when(promptManager.getChatSystemPrompt()).thenReturn(new SystemPrompt("SYSTEM_PROMPT"));

        when(chatOpenAiProperties.getModel()).thenReturn("gpt-4.1");
        when(chatOpenAiProperties.getTemperature()).thenReturn(0.5);
        when(chatOpenAiProperties.getMaxTokens()).thenReturn(500);

        ChatSummaries emptySummaries = new ChatSummaries(List.of());
        ChattingContext context = new ChattingContext(List.of(new Chatting(1L, "hello", Sender.USER)));

        //when
        ChatCompletionRequest request = factory.buildChatRequest(context, emptySummaries);

        //then
        List<ChatCompletionRequest.Message> messages = request.getMessages();

        assertThat(messages).hasSize(2);
        assertThat(messages.get(1).getRole()).isEqualTo("user");
        assertThat(messages.get(1).getContent()).isEqualTo("hello");
    }

    @Test
    @DisplayName("요약 ChatCompletionRequest 생성 테스트")
    void 요약_생성_테스트() {
        //given
        SystemPrompt summarySystemPrompt = new SystemPrompt("요약");
        when(promptManager.getSummarySystemPrompt()).thenReturn(summarySystemPrompt);

        when(chatSummaryOpenAiProperties.getModel()).thenReturn("모델");
        when(chatSummaryOpenAiProperties.getTemperature()).thenReturn(0.3);
        when(chatSummaryOpenAiProperties.getMaxTokens()).thenReturn(300);

        Chatting chat1 = new Chatting(6L, "첫번째", Sender.USER);
        Chatting chat2 = new Chatting(6L, "두번째", Sender.AI);

        List<Chatting> chattings = List.of(chat1, chat2);

        //when
        ChatCompletionRequest request = factory.buildSummaryRequest(chattings);

        //then
        assertThat(request.getModel()).isEqualTo("모델");
        assertThat(request.getTemperature()).isEqualTo(0.3);
        assertThat(request.getMax_tokens()).isEqualTo(300);

        //messages 검증
        List<ChatCompletionRequest.Message> messages = request.getMessages();
        assertThat(messages).hasSize(3); //system + user + assistant

        assertThat(messages.get(0).getRole()).isEqualTo("system");
        assertThat(messages.get(0).getContent()).isEqualTo("요약");

        assertThat(messages.get(1).getRole()).isEqualTo("user");
        assertThat(messages.get(1).getContent()).isEqualTo("첫번째");

        assertThat(messages.get(2).getRole()).isEqualTo("assistant");
        assertThat(messages.get(2).getContent()).isEqualTo("두번째");
    }

    @Test
    @DisplayName("ACT 추천 ChatCompletionRequest 생성 테스트")
    void ACT_추천_생성_테스트() {
        //given
        SystemPrompt actSystemPrompt = new SystemPrompt("ACT 추천 시스템 프롬프트");
        when(promptManager.getActRecommendPrompt()).thenReturn(actSystemPrompt);

        when(actRecommendOpenAiProperties.getModel()).thenReturn("ACT-모델");
        when(actRecommendOpenAiProperties.getTemperature()).thenReturn(0.2);
        when(actRecommendOpenAiProperties.getMaxTokens()).thenReturn(500);

        ChatSummary chatSummary = new ChatSummary(1L, 2L, "이 사용자는 불안과 회피 행동을 보이고 있음");

        //when
        ChatCompletionRequest request = factory.buildACTRecommendRequest(chatSummary);

        //then
        assertThat(request.getModel()).isEqualTo("ACT-모델");
        assertThat(request.getTemperature()).isEqualTo(0.2);
        assertThat(request.getMax_tokens()).isEqualTo(500);

        //messages 검증
        List<ChatCompletionRequest.Message> messages = request.getMessages();
        assertThat(messages).hasSize(2); //system + summary(user)

        assertThat(messages.get(0).getRole()).isEqualTo("system");
        assertThat(messages.get(0).getContent()).isEqualTo("ACT 추천 시스템 프롬프트");

        assertThat(messages.get(1).getRole()).isEqualTo("user");
        assertThat(messages.get(1).getContent()).isEqualTo("이 사용자는 불안과 회피 행동을 보이고 있음");
    }

}
