package com.nyangtodac.act;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nyangtodac.act.application.ACTRecordConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ACTRecordConverterTest {

    private ACTRecordConverter converter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        converter = new ACTRecordConverter(objectMapper);
    }

    @Test
    @DisplayName("JsonNode를 Map으로 변환한다")
    void jsonNode_to_map() throws Exception {
        //given
        JsonNode jsonNode = objectMapper.readTree("""
        {
          "emotion": "sad",
          "intensity": 3,
          "meta": {
            "source": "user"
          }
        }
        """);

        //when
        Map<String, Object> result = converter.convertToMap(jsonNode);

        //then
        assertThat(result).containsEntry("emotion", "sad");
        assertThat(result).containsEntry("intensity", 3);

        Map<String, Object> meta = (Map<String, Object>) result.get("meta");
        assertThat(meta).containsEntry("source", "user");
    }

    @Test
    @DisplayName("Map을 JsonNode로 변환한다")
    void map_to_jsonNode() {
        //given
        Map<String, Object> map = Map.of(
                "situation", "회의 중 실수",
                "score", 2,
                "nested", Map.of("key", "value")
        );

        //when
        JsonNode jsonNode = converter.convertToJsonNode(map);

        //then
        assertThat(jsonNode.get("situation").asText()).isEqualTo("회의 중 실수");
        assertThat(jsonNode.get("score").asInt()).isEqualTo(2);
        assertThat(jsonNode.get("nested").get("key").asText()).isEqualTo("value");
    }
}
