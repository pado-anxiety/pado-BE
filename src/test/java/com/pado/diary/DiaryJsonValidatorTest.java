package com.pado.diary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.act.application.InvalidFormatException;
import com.pado.act.YamlPropertySourceFactory;
import com.pado.diary.application.validator.DiaryJsonValidator;
import com.pado.diary.application.validator.DiaryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DiaryJsonValidatorTest.TestConfig.class)
@EnableConfigurationProperties(DiaryProperties.class)
@TestPropertySource(locations = "classpath:format.yml", factory = YamlPropertySourceFactory.class)
public class DiaryJsonValidatorTest {

    @Autowired
    DiaryProperties diaryProperties;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DiaryProperties diaryProperties() {
            return new DiaryProperties();
        }
    }

    private DiaryJsonValidator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        validator = new DiaryJsonValidator(diaryProperties);
    }

    @Test
    @DisplayName("정상 데이터 통과")
    void 정상_데이터_통과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날 밤에 잠을 못 잤다",
          "thoughts": "내일 망하면 어떡하지",
          "feelings": ["불안", "두려움"]
        }
        """);

        assertThatCode(() -> validator.validate(json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("data가 null이면 예외")
    void data가_null이면_예외() {
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(InvalidFormatException.class);
    }

    @Test
    @DisplayName("data가 JSON 객체가 아닌 경우 예외")
    void data가_JSON_객체가_아니면_예외() throws Exception {
        JsonNode json = objectMapper.readTree("\"문자열\"");

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class);
    }

    @Test
    @DisplayName("situation 필드 누락 시 예외")
    void situation_필드_누락() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "thoughts": "걱정이 많다",
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("situation");
    }

    @Test
    @DisplayName("situation 필드가 문자열이 아닌 경우 예외")
    void situation_필드_문자열_아님() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": 123,
          "thoughts": "걱정이 많다",
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class);
    }

    @Test
    @DisplayName("situation 필드가 공백만 있으면 예외")
    void situation_필드_공백() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "   ",
          "thoughts": "걱정이 많다",
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("비어 있을 수 없습니다");
    }

    @Test
    @DisplayName("situation 필드가 200자이면 통과")
    void situation_필드_200자_통과() throws Exception {
        String exactly200 = "가".repeat(200);
        String jsonStr = String.format("""
        {
          "situation": "%s",
          "thoughts": "걱정이 많다",
          "feelings": ["불안"]
        }
        """, exactly200);

        assertThatCode(() -> validator.validate(objectMapper.readTree(jsonStr))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("situation 필드가 200자 초과 시 예외")
    void situation_필드_200자_초과() throws Exception {
        String over200 = "가".repeat(201);
        String jsonStr = String.format("""
        {
          "situation": "%s",
          "thoughts": "걱정이 많다",
          "feelings": ["불안"]
        }
        """, over200);

        assertThatThrownBy(() -> validator.validate(objectMapper.readTree(jsonStr))).isInstanceOf(InvalidFormatException.class).hasMessageContaining("200");
    }

    // ───────────── thoughts ─────────────

    @Test
    @DisplayName("thoughts 필드 누락 시 예외")
    void thoughts_필드_누락() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("thoughts");
    }

    @Test
    @DisplayName("thoughts 필드가 문자열이 아닌 경우 예외")
    void thoughts_필드_문자열_아님() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": true,
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class);
    }

    @Test
    @DisplayName("thoughts 필드가 공백만 있으면 예외")
    void thoughts_필드_공백() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "   ",
          "feelings": ["불안"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("비어 있을 수 없습니다");
    }

    @Test
    @DisplayName("thoughts 필드가 200자 초과 시 예외")
    void thoughts_필드_200자_초과() throws Exception {
        String over200 = "나".repeat(201);
        String jsonStr = String.format("""
        {
          "situation": "발표 전날",
          "thoughts": "%s",
          "feelings": ["불안"]
        }
        """, over200);

        assertThatThrownBy(() -> validator.validate(objectMapper.readTree(jsonStr))).isInstanceOf(InvalidFormatException.class).hasMessageContaining("200");
    }

    @Test
    @DisplayName("feelings 필드 누락 시 예외")
    void feelings_필드_누락() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "걱정이 많다"
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("feelings");
    }

    @Test
    @DisplayName("feelings 필드가 배열이 아닌 경우 예외")
    void feelings_필드_배열_아님() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "걱정이 많다",
          "feelings": "불안"
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("배열");
    }

    @Test
    @DisplayName("feelings 배열이 비어있으면 예외")
    void feelings_배열_비어있음() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "걱정이 많다",
          "feelings": []
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("비어 있을 수 없습니다");
    }

    @Test
    @DisplayName("feelings 배열이 3개이면 통과")
    void feelings_배열_최대_3개_통과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "걱정이 많다",
          "feelings": ["불안", "두려움", "긴장"]
        }
        """);

        assertThatCode(() -> validator.validate(json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("feelings 배열이 3개 초과 시 예외")
    void feelings_배열_3개_초과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "발표 전날",
          "thoughts": "걱정이 많다",
          "feelings": ["불안", "두려움", "긴장", "걱정"]
        }
        """);

        assertThatThrownBy(() -> validator.validate(json)).isInstanceOf(InvalidFormatException.class).hasMessageContaining("3");
    }
}