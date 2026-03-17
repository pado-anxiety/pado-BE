package com.pado.util.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonMapConverter {

    private final ObjectMapper objectMapper;

    public Map<String, Object> convertToMap(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, new TypeReference<>() {});
    }

    public JsonNode convertToJsonNode(Map<String, Object> map) {
        return objectMapper.valueToTree(map);
    }
}
