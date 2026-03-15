package com.pado.diary.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.diary.controller.dto.DiaryCalendarItem;
import com.pado.diary.controller.dto.DiaryResponse;
import com.pado.diary.infrastructure.DiaryEntity;
import com.pado.util.converter.JsonMapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class DiaryConverter {

    private final JsonMapConverter jsonMapConverter;

    public DiaryResponse toResponse(DiaryEntity entity, ZoneId zoneId) {
        JsonNode data = jsonMapConverter.convertToJsonNode(entity.getData());
        return new DiaryResponse(convertToLocalDateTime(entity.getCreatedAt(), zoneId), data.get("situation").asText(), data.get("thoughts").asText(), extractFeelings(data));
    }

    private LocalDateTime convertToLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public List<DiaryCalendarItem> toCalendarItems(List<DiaryEntity> entities, ZoneId zoneId) {
        return entities.stream()
                .sorted(Comparator.comparing(DiaryEntity::getCreatedAt))
                .map(e -> {
            JsonNode data = jsonMapConverter.convertToJsonNode(e.getData());
            return new DiaryCalendarItem(e.getId(), convertToLocalDateTime(e.getCreatedAt(), zoneId), extractFeelings(data));
        }).toList();
    }

    private List<String> extractFeelings(JsonNode data) {
        return StreamSupport.stream(data.get("feelings").spliterator(), false).map(JsonNode::asText).toList();
    }
}
