package com.pado.act.application.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.ACTType;
import com.pado.act.application.Diagnosis;
import com.pado.act.application.InvalidFormatException;
import com.pado.act.application.validator.properties.AcceptanceProperties;
import com.pado.act.application.validator.properties.CognitiveDefusionProperties;
import com.pado.act.application.validator.properties.CommittedActionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ACTRecordJsonValidator {

    private final CognitiveDefusionProperties cognitiveDefusionProperties;
    private final CommittedActionProperties committedActionProperties;
    private final AcceptanceProperties acceptanceProperties;

    public void validate(ACTType actType, JsonNode data) {
        validateJsonNode(data);
        switch (actType) {
            case COGNITIVE_DEFUSION -> validateCognitiveDefusionData(data);
            case ACCEPTANCE -> validateAcceptanceData(data);
            case COMMITTED_ACTION -> validateCommittedActionData(data);
        }
    }

    private void validateCognitiveDefusionData(JsonNode data) {
        JsonNode tokens = data.get("userTextToken");
        if (tokens == null) {
            throw new InvalidFormatException("userTextToken 필드가 존재하지 않습니다.");
        }
        if (!tokens.isArray()) {
            throw new InvalidFormatException("userTextToken 필드는 배열이어야 합니다.");
        }
        if (tokens.isEmpty()) {
            throw new InvalidFormatException("userTextToken 배열은 비어 있을 수 없습니다.");
        }

        int stringCount = tokens.size() - 1;
        for (int i = 0; i < tokens.size(); i++) {
            JsonNode token = tokens.get(i);
            if (!token.isObject()) {
                throw new InvalidFormatException(
                        "userTextToken[" + i + "] 는 객체여야 합니다."
                );
            }

            JsonNode text = token.get("text");
            if (text == null || !text.isTextual() || text.asText().trim().isEmpty()) {
                throw new InvalidFormatException(
                        "userTextToken[" + i + "].text 는 비어 있지 않은 문자열이어야 합니다."
                );
            }
            stringCount += text.asText().trim().length();

            JsonNode isSelected = token.get("isSelected");
            if (isSelected == null || !isSelected.isBoolean()) {
                throw new InvalidFormatException(
                        "userTextToken[" + i + "].isSelected 는 boolean 이어야 합니다."
                );
            }
        }

        if (stringCount > cognitiveDefusionProperties.getTotalTextLength()) {
            throw new InvalidFormatException("text의 총 길이는 " + cognitiveDefusionProperties.getTotalTextLength() + " 이하여야 합니다.");
        }
    }

    private void validateCommittedActionData(JsonNode data) {
        JsonNode diagnosis = data.get("diagnosis");
        if (diagnosis == null || !diagnosis.isObject()) {
            throw new InvalidFormatException("diagnosis 필드는 객체여야 합니다.");
        }

        validateDiagnosisInt(diagnosis, "work");
        validateDiagnosisInt(diagnosis, "growth");
        validateDiagnosisInt(diagnosis, "leisure");
        validateDiagnosisInt(diagnosis, "relationship");

        JsonNode matter = data.get("matter");
        if (matter == null || !matter.isTextual()) {
            throw new InvalidFormatException("matter 필드는 문자열이어야 합니다.");
        }
        try {
            Diagnosis.valueOf(matter.asText());
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("matter 값이 올바르지 않습니다.");
        }

        validateStringField(data, "value", committedActionProperties.getValue().getLength());
        validateStringField(data, "barrier", committedActionProperties.getBarrier().getLength());
        validateStringField(data, "action", committedActionProperties.getAction().getLength());
    }


    private void validateAcceptanceData(JsonNode data) {
        if (!data.has("breathingTime")) {
            throw new InvalidFormatException("breathingTime 필드가 존재하지 않습니다.");
        }
        JsonNode time = data.get("breathingTime");
        if (!time.isInt()) {
            throw new InvalidFormatException("breathingTime 필드는 정수여야 합니다.");
        }
        if (time.asInt() < acceptanceProperties.getBreathingTime().getMin()) {
            throw new InvalidFormatException("breathingTime 필드는 " + acceptanceProperties.getBreathingTime().getMin() + " 이상이여야 합니다.");
        }
    }

    private void validateStringField(JsonNode data, String fieldName, int length) {
        if (!data.has(fieldName)) {
            throw new InvalidFormatException(fieldName + " 필드가 존재하지 않습니다.");
        }

        JsonNode field = data.get(fieldName);

        if (!field.isTextual()) {
            throw new InvalidFormatException(fieldName + " 필드는 문자열이어야 합니다.");
        }

        String fieldValue = field.asText().trim();
        if (fieldValue.isEmpty()) {
            throw new InvalidFormatException(fieldName + " 필드는 비어 있을 수 없습니다.");
        }

        if (fieldValue.length() > length) {
            throw new InvalidFormatException(fieldName + " 필드의 길이가 " + length + "자를 초과하였습니다.");
        }
    }

    private void validateDiagnosisInt(JsonNode data, String fieldName) {
        if (!data.has(fieldName)) {
            throw new InvalidFormatException(fieldName + " 필드가 존재하지 않습니다.");
        }

        JsonNode field = data.get(fieldName);

        if (!field.isInt()) {
            throw new InvalidFormatException(fieldName + " 필드는 정수여야 합니다.");
        }
        int anInt = field.asInt();
        if (anInt < committedActionProperties.getDiagnosis().getMin() || anInt > committedActionProperties.getDiagnosis().getMax()) {
            throw new InvalidFormatException("diagnosis." + fieldName + " 값의 범위는 " + committedActionProperties.getDiagnosis().getMin() + "~" + committedActionProperties.getDiagnosis().getMax() + " 사이여야 합니다.");
        }
    }

    private void validateJsonNode(JsonNode data) {
        if (data == null || !data.isObject()) {
            throw new InvalidFormatException("요청 데이터는 JSON 객체여야 합니다.");
        }
    }
}
