package com.pado.diary.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.diary.application.validator.DiaryJsonValidator;
import com.pado.diary.infrastructure.DiaryEntity;
import com.pado.diary.infrastructure.DiaryRepository;
import com.pado.util.converter.JsonMapConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryJsonValidator diaryJsonValidator;
    private final DiaryRepository diaryRepository;

    private final JsonMapConverter jsonMapConverter;

    public void saveDiary(Long userId, JsonNode jsonNode) {
        diaryJsonValidator.validate(jsonNode);
        Map<String, Object> map = jsonMapConverter.convertToMap(jsonNode);
        diaryRepository.save(new DiaryEntity(userId, map));
    }
}
