package com.pado.diary.application.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.application.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(DiaryProperties.class)
public class DiaryJsonValidator {

    private final DiaryProperties diaryProperties;

    public void validate(JsonNode data) {
        validateJsonNode(data);
        validateStringField(data, "situation", diaryProperties.getSituation().getLength());
        validateStringField(data, "thoughts", diaryProperties.getThoughts().getLength());

        if (!data.has("feelings")) {
            throw new InvalidFormatException("feelings 필드가 존재하지 않습니다.");
        }
        JsonNode feelings = data.get("feelings");
        if (!feelings.isArray()) {
            throw new InvalidFormatException("feelings 필드는 배열이어야 합니다.");
        }
        if (feelings.isEmpty()) {
            throw new InvalidFormatException("feelings 배열은 비어 있을 수 없습니다.");
        }

        if (feelings.size() > diaryProperties.getFeelings().getMax()) {
            throw new InvalidFormatException("feelings 배열의 요소는 최대 " + diaryProperties.getFeelings().getMax() + "개 까지 포함할 수 있습니다.");
        }

        for (JsonNode feeling : feelings) {
            if (!feeling.isTextual()) {
                throw new InvalidFormatException("feelings 배열의 요소는 문자열이어야 합니다.");
            }
            if (feeling.asText().trim().isEmpty()) {
                throw new InvalidFormatException("feelings 배열의 요소는 비어 있을 수 없습니다.");
            }
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

    private void validateJsonNode(JsonNode data) {
        if (data == null || !data.isObject()) {
            throw new InvalidFormatException("요청 데이터는 JSON 객체여야 합니다.");
        }
    }
}
