package com.pado.diary.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.diary.controller.dto.DiaryResponse;
import com.pado.diary.infrastructure.DiaryEntity;
import com.pado.util.converter.JsonMapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryConverter {

    private final JsonMapConverter jsonMapConverter;

    public DiaryResponse toResponse(DiaryEntity entity, ZoneId zoneId) {
        JsonNode data = jsonMapConverter.convertToJsonNode(entity.getData());
        List<String> feelings = new ArrayList<>();
        data.get("feelings").forEach(node -> feelings.add(node.asText()));
        return new DiaryResponse(convertToLocalDateTime(entity.getCreatedAt(), zoneId), data.get("situation").asText(), data.get("thoughts").asText(), feelings);
    }

    private LocalDateTime convertToLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
