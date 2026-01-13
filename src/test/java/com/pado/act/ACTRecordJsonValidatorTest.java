package com.pado.act;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pado.act.application.InvalidActRecordRequestException;
import com.pado.act.application.validator.ACTRecordJsonValidator;
import com.pado.act.application.validator.properties.CognitiveDefusionProperties;
import com.pado.act.application.validator.properties.CommittedActionProperties;
import com.pado.act.application.validator.properties.EmotionNoteProperties;
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
@ContextConfiguration(classes= ACTRecordJsonValidatorTest.TestConfig.class)
@EnableConfigurationProperties({EmotionNoteProperties.class, CognitiveDefusionProperties.class, CommittedActionProperties.class})
@TestPropertySource(locations = "classpath:act-format.yml", factory = YamlPropertySourceFactory.class)
public class ACTRecordJsonValidatorTest {

    @Autowired
    EmotionNoteProperties emotionNoteProperties;

    @Autowired
    CognitiveDefusionProperties cognitiveDefusionProperties;

    @Autowired
    CommittedActionProperties committedActionProperties;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EmotionNoteProperties emotionNoteProperties() {
            return new EmotionNoteProperties();
        }

        @Bean
        public CognitiveDefusionProperties cognitiveDefusionProperties() {
            return new CognitiveDefusionProperties();
        }

        @Bean
        public CommittedActionProperties committedActionProperties() {
            return new CommittedActionProperties();
        }
    }

    private ACTRecordJsonValidator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        validator = new ACTRecordJsonValidator(emotionNoteProperties, cognitiveDefusionProperties, committedActionProperties);
    }

    @Test
    @DisplayName("yaml load test")
    void testYamlLoad() {
        assertThat(emotionNoteProperties.getSituation().getLength()).isEqualTo(500);
        assertThat(emotionNoteProperties.getFeelings().getLength()).isEqualTo(500);
    }

    @Test
    @DisplayName("EMOTION_NOTE - 정상 데이터 통과")
    void emotion_note_정상_통과() throws Exception {
        // given
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "회사에서 실수함",
          "thoughts": "나는 무능하다",
          "feelings": "불안"
        }
        """);

        // when & then
        assertThatCode(() -> validator.validate(ACTType.EMOTION_NOTE, json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("EMOTION_NOTE - 필수 필드 누락 시 예외")
    void emotion_note_필드_누락() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "situation": "상황만 있음"
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.EMOTION_NOTE, json)).isInstanceOf(InvalidActRecordRequestException.class).hasMessageContaining("thoughts");
    }

    @Test
    @DisplayName("COGNITIVE_DEFUSION - 정상 데이터 통과")
    void cognitive_defusion_정상_통과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "userTextToken": [
            { "text": "나는 실패자다", "isSelected": true },
            { "text": "이 생각은 사실이 아니다", "isSelected": false }
          ]
        }
        """);

        assertThatCode(() -> validator.validate(ACTType.COGNITIVE_DEFUSION, json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("COGNITIVE_DEFUSION - userTextToken 배열이 비어있으면 예외")
    void cognitive_defusion_빈_배열() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "userTextToken": []
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.COGNITIVE_DEFUSION, json)).isInstanceOf(InvalidActRecordRequestException.class).hasMessageContaining("비어 있을 수 없습니다");
    }

    @Test
    @DisplayName("COGNITIVE_DEFUSION - 총 text의 길이 100글자")
    void cognitive_defusion_총_text_길이가_100글자() throws JsonProcessingException {
        JsonNode json = objectMapper.readTree("""
        {
          "userTextToken": [
            { "text": "차분한 오후에 커피 향이 번지고, 창가의 빛 속에서 우리는 작은 목표를 세우며 내일을 준비한다.", "isSelected": true },
            { "text": "조용한 음악과 함께 호흡을 고르며 오늘의 배움을 마음에 새긴다. 그리고 미소짓는다!", "isSelected": false }
          ]
        }
        """);

        assertThatCode(() -> validator.validate(ACTType.COGNITIVE_DEFUSION, json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("COGNITIVE_DEFUSION - 총 text의 길이 100글자 초과 시 예외")
    void cognitive_defusion_총_text_길이가_100글자_초과() throws JsonProcessingException {
        JsonNode json = objectMapper.readTree("""
        {
          "userTextToken": [
            { "text": "차분한 오후에 커피 향이 번지고, 창가의 빛 속에서 우리는 작은 목표를 세우며 내일을 준비한다.", "isSelected": true },
            { "text": "조용한 음악과 함께 호흡을 고르며 오늘의 배움을 마음에 새긴다. 그리고 미소짓는다!!", "isSelected": false }
          ]
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.COGNITIVE_DEFUSION, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }

    @Test
    @DisplayName("ACCEPTANCE - breathingTime 정상 통과")
    void acceptance_정상_통과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "breathingTime": 3
        }
        """);

        assertThatCode(() -> validator.validate(ACTType.ACCEPTANCE, json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ACCEPTANCE - breathingTime이 정수가 아닌 경우 예외")
    void acceptance_breathingTime이_정수_아닌_경우() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "breathingTime": "3"
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.ACCEPTANCE, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }

    @Test
    @DisplayName("ACCEPTANCE - breathingTime이 없는 경우 예외")
    void acceptance_breathingTime이_없는_경우() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
        
        }
        """);
        assertThatThrownBy(() -> validator.validate(ACTType.ACCEPTANCE, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }

    @Test
    @DisplayName("COMMITTED_ACTION - 정상 데이터 통과")
    void committed_action_정상_통과() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "diagnosis": {
            "work": 3,
            "growth": 1,
            "leisure": 2,
            "relationship": 2
          },
          "matter": "WORK",
          "value": "성장",
          "barrier": "시간 부족",
          "action": "하루 30분 공부"
        }
        """);

        assertThatCode(() -> validator.validate(ACTType.COMMITTED_ACTION, json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("COMMITTED_ACTION - diagnosis 값이 1 미만이면 예외")
    void committed_action_diagnosis_범위_오류1() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "diagnosis": {
            "work": 0,
            "growth": 2,
            "leisure": 3,
            "relationship": 3
          },
          "matter": "WORK",
          "value": "가치",
          "barrier": "장애물",
          "action": "행동"
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.COMMITTED_ACTION, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }

    @Test
    @DisplayName("COMMITTED_ACTION - diagnosis 값이 3 초과면 예외")
    void committed_action_diagnosis_범위_오류2() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "diagnosis": {
            "work": 1,
            "growth": 2,
            "leisure": 3,
            "relationship": 4
          },
          "matter": "WORK",
          "value": "가치",
          "barrier": "장애물",
          "action": "행동"
        }
        """);

        assertThatThrownBy(() -> validator.validate(ACTType.COMMITTED_ACTION, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }

    @Test
    @DisplayName("COMMITTED_ACTION - 필수 필드 누락 시 예외")
    void committed_action_diagnosis_필드_누락() throws Exception {
        JsonNode json = objectMapper.readTree("""
        {
          "diagnosis": {
            "work": 1,
            "growth": 2,
            "leisure": 3,
            "relationship": 4
          },
          "value": "가치",
          "barrier": "장애물",
          "action": "행동"
        }
        """);
        //matter 누락

        assertThatThrownBy(() -> validator.validate(ACTType.COMMITTED_ACTION, json)).isInstanceOf(InvalidActRecordRequestException.class);
    }




}
