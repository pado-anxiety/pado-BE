package com.pado.chat;

import com.pado.chat.domain.ChatSummaries;
import com.pado.chat.domain.ChatSummary;
import com.pado.external.ai.infrastructure.prompt.PromptManager;
import com.pado.external.ai.infrastructure.prompt.SystemPrompt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptManagerTest {

    @Mock
    SystemPrompt summaryPrefix;

    PromptManager promptManager;

    @BeforeEach
    void setUp() {
        promptManager = new PromptManager(
                mock(SystemPrompt.class),
                mock(SystemPrompt.class),
                summaryPrefix,
                mock(SystemPrompt.class)
        );
    }

    @Test
    @DisplayName("promptManager makeSummaryPrompt 테스트 - 요약이 있으면 prefix와 합쳐진다")
    void summary가_있으면_prefix와_합쳐진다() {
        //given
        when(summaryPrefix.getSystem()).thenReturn("요약 prefix");
        ChatSummaries summaries = new ChatSummaries(List.of(new ChatSummary(1L, 2L, "요약1"), new ChatSummary(3L, 4L, "요약2")));

        //when
        Optional<String> result = promptManager.makeSummaryPrompt(summaries);

        //then
        assertThat(result).contains("""
                요약 prefix
                요약1
                
                요약2
                """.trim());
    }

    @Test
    @DisplayName("promptManager makeSummaryPrompt 테스트 - 요약이 없으면 optional.empty를 반환한다")
    void summary가_없으면_empty() {
        ChatSummaries summaries = new ChatSummaries(List.of());
        Optional<String> result = promptManager.makeSummaryPrompt(summaries);
        assertThat(result).isEmpty();
    }
}

