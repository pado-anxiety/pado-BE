package com.pado.chat;

import com.pado.chat.controller.dto.Sender;
import com.pado.chat.domain.Chatting;
import com.pado.chat.domain.ChattingContext;
import com.pado.external.ai.infrastructure.ChatCompletionMessageConverter;
import com.pado.external.ai.infrastructure.ChatCompletionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatCompletionMessageConverterTest {
    
    ChatCompletionMessageConverter converter = new ChatCompletionMessageConverter();

    @Test
    @DisplayName("List<Chatting> -> List<ChatCompletionRequest.Message> 변환")
    void convert_Chatting_리스트를_Message_리스트로_변환() {
        //given
        List<Chatting> chattings = List.of(
            new Chatting("질문입니다", Sender.USER),
            new Chatting("답변입니다", Sender.AI)
        );

        //when
        List<ChatCompletionRequest.Message> messages = converter.convert(chattings);

        //then
        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getRole()).isEqualTo("user");
        assertThat(messages.get(0).getContent()).isEqualTo("질문입니다");
        assertThat(messages.get(1).getRole()).isEqualTo("assistant");
        assertThat(messages.get(1).getContent()).isEqualTo("답변입니다");
    }

    @Test
    @DisplayName("ChattingContext -> List<ChatCompletionRequest.Message> 변환")
    void convert_ChattingContext를_Message_리스트로_변환() {
        //given
        ChattingContext context = new ChattingContext(List.of(new Chatting("안녕", Sender.USER)));

        //when
        List<ChatCompletionRequest.Message> messages = converter.convert(context);

        //then
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).getRole()).isEqualTo("user");
    }
}