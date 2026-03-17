package com.pado.diary.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.application.ResourceAccessDeniedException;
import com.pado.diary.application.validator.DiaryJsonValidator;
import com.pado.diary.controller.dto.DiaryCalendarItem;
import com.pado.diary.controller.dto.DiaryResponse;
import com.pado.diary.infrastructure.DiaryEntity;
import com.pado.diary.infrastructure.DiaryRepository;
import com.pado.util.converter.JsonMapConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryJsonValidator diaryJsonValidator;
    private final DiaryRepository diaryRepository;
    private final DiaryConverter diaryConverter;

    private final JsonMapConverter jsonMapConverter;

    public void saveDiary(Long userId, JsonNode jsonNode) {
        diaryJsonValidator.validate(jsonNode);
        Map<String, Object> map = jsonMapConverter.convertToMap(jsonNode);
        diaryRepository.save(new DiaryEntity(userId, map));
    }

    public DiaryResponse getDiary(Long userId, ZoneId zoneId, Long id) {
        DiaryEntity diary = getById(id);
        if (!diary.getUserId().equals(userId)) {
            throw new ResourceAccessDeniedException();
        }
        return diaryConverter.toResponse(diary, zoneId);
    }

    public List<DiaryCalendarItem> getCalendarItems(Long userId, ZoneId zoneId, Integer year, Integer month) {
        ZonedDateTime startOfMonth = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, zoneId);
        ZonedDateTime endOfMonth = startOfMonth.plusMonths(1);

        Instant from = startOfMonth.toInstant();
        Instant to = endOfMonth.toInstant();

        return diaryConverter.toCalendarItems(diaryRepository.findByUserIdAndCreatedAtBetween(userId, from, to), zoneId);
    }

    private DiaryEntity getById(Long id) {
        return diaryRepository.findById(id).orElseThrow(() -> new DiaryNotFoundException(id));
    }
}
