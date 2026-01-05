package com.nyangtodac.act.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.nyangtodac.act.ACTType;
import org.springframework.stereotype.Component;

@Component
public class ACTRecordJsonValidator {

    public void validate(ACTType actType, JsonNode data) {
        validateJsonNode(data);
        switch (actType) {
            case EMOTION_NOTE -> validateEmotionNoteData(data);
            case COGNITIVE_DEFUSION -> validateCognitiveDefusionData(data);
            case ACCEPTANCE -> validateAcceptanceData(data);
        }
    }

    private void validateEmotionNoteData(JsonNode data) {
        validateStringField(data, "situation");
        validateStringField(data, "thoughts");
        validateStringField(data, "feelings");
    }

    private void validateCognitiveDefusionData(JsonNode data) {
        JsonNode tokens = data.get("userTextToken");
        if (tokens == null) {
            throw new InvalidActRecordRequestException("userTextToken 필드가 존재하지 않습니다.");
        }
        if (!tokens.isArray()) {
            throw new InvalidActRecordRequestException("userTextToken 필드는 배열이어야 합니다.");
        }
        if (tokens.isEmpty()) {
            throw new InvalidActRecordRequestException("userTextToken 배열은 비어 있을 수 없습니다.");
        }

        for (int i = 0; i < tokens.size(); i++) {
            JsonNode token = tokens.get(i);
            if (!token.isObject()) {
                throw new InvalidActRecordRequestException(
                        "userTextToken[" + i + "] 는 객체여야 합니다."
                );
            }

            JsonNode text = token.get("text");
            if (text == null || !text.isTextual() || text.asText().trim().isEmpty()) {
                throw new InvalidActRecordRequestException(
                        "userTextToken[" + i + "].text 는 비어 있지 않은 문자열이어야 합니다."
                );
            }

            JsonNode isSelected = token.get("isSelected");
            if (isSelected == null || !isSelected.isBoolean()) {
                throw new InvalidActRecordRequestException(
                        "userTextToken[" + i + "].isSelected 는 boolean 이어야 합니다."
                );
            }
        }
    }

    private void validateAcceptanceData(JsonNode data) {
        validateStringField(data, "breathingTime");
    }

    private void validateStringField(JsonNode data, String fieldName) {
        if (!data.has(fieldName)) {
            throw new InvalidActRecordRequestException(fieldName + " 필드가 존재하지 않습니다.");
        }

        JsonNode field = data.get(fieldName);

        if (!field.isTextual()) {
            throw new InvalidActRecordRequestException(fieldName + " 필드는 문자열이어야 합니다.");
        }

        if (field.asText().trim().isEmpty()) {
            throw new InvalidActRecordRequestException(fieldName + " 필드는 비어 있을 수 없습니다.");
        }
    }

    private void validateJsonNode(JsonNode data) {
        if (data == null || !data.isObject()) {
            throw new InvalidActRecordRequestException("요청 데이터는 JSON 객체여야 합니다.");
        }
    }
}
