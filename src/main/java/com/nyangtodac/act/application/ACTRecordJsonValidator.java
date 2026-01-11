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
            case COMMITTED_ACTION -> validateCommittedActionData(data);
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

    private void validateCommittedActionData(JsonNode data) {
        JsonNode diagnosis = data.get("diagnosis");
        if (diagnosis == null || !diagnosis.isObject()) {
            throw new InvalidActRecordRequestException("diagnosis 필드는 객체여야 합니다.");
        }

        validateDiagnosisInt(diagnosis, "work");
        validateDiagnosisInt(diagnosis, "growth");
        validateDiagnosisInt(diagnosis, "leisure");
        validateDiagnosisInt(diagnosis, "relationship");

        JsonNode matter = data.get("matter");
        if (matter == null || !matter.isTextual()) {
            throw new InvalidActRecordRequestException("matter 필드는 문자열이어야 합니다.");
        }
        try {
            Diagnosis.valueOf(matter.asText());
        } catch (IllegalArgumentException e) {
            throw new InvalidActRecordRequestException("matter 값이 올바르지 않습니다.");
        }

        validateStringField(data, "value");
        validateStringField(data, "barrier");
        validateStringField(data, "action");
    }


    private void validateAcceptanceData(JsonNode data) {
        if (!data.has("breathingTime")) {
            throw new InvalidActRecordRequestException("breathingTime 필드가 존재하지 않습니다.");
        }
        JsonNode time = data.get("breathingTime");
        if (!time.isInt()) {
            throw new InvalidActRecordRequestException("breathingTime 필드는 정수여야 합니다.");
        }
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

    private void validateDiagnosisInt(JsonNode data, String fieldName) {
        if (!data.has(fieldName)) {
            throw new InvalidActRecordRequestException(fieldName + " 필드가 존재하지 않습니다.");
        }

        JsonNode field = data.get(fieldName);

        if (!field.isInt()) {
            throw new InvalidActRecordRequestException(fieldName + " 필드는 정수여야 합니다.");
        }
        int anInt = field.asInt();
        if (anInt < 1 || anInt > 3) {
            throw new InvalidActRecordRequestException("diagnosis." + fieldName + " 값의 범위는 1~3 사이여야 합니다.");
        }
    }

    private void validateJsonNode(JsonNode data) {
        if (data == null || !data.isObject()) {
            throw new InvalidActRecordRequestException("요청 데이터는 JSON 객체여야 합니다.");
        }
    }
}
