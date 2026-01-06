package com.nyangtodac.act.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.nyangtodac.act.ACTType;
import com.nyangtodac.act.controller.dto.ACTRecordResponse;
import com.nyangtodac.act.controller.dto.ACTRecords;
import com.nyangtodac.act.infrastructure.ACTRecordEntity;
import com.nyangtodac.act.infrastructure.ACTRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ACTRecordService {

    private final ACTRecordRepository actRecordRepository;
    private final ACTRecordJsonValidator jsonValidator;
    private final ACTRecordConverter actRecordConverter;

    private static final int ACT_RECORD_PAGE_SIZE = 20;

    @Transactional(readOnly = true)
    public ACTRecords findAllActRecords(Long userId, Long cursor) {
        if (cursor == null) {
            cursor = Long.MAX_VALUE;
        }
        List<ACTRecordEntity> entities = actRecordRepository.findAllByUserIdAndIdLessThanOrderByTimeDesc(userId, cursor, PageRequest.of(0, ACT_RECORD_PAGE_SIZE));
        return ACTRecordMapper.toACTRecords(entities);
    }

    @Transactional(readOnly = true)
    public ACTRecordResponse findACTRecordResponse(Long userId, Long recordId) {
        ACTRecordEntity entity = actRecordRepository.findById(recordId).orElseThrow(() -> new ACTRecordNotFoundException(recordId));
        if (!entity.getUserId().equals(userId)) {
            throw new ACTAccessDeniedException();
        }
        return new ACTRecordResponse(entity.getId(), entity.getTime(), entity.getActType(), actRecordConverter.convertToJsonNode(entity.getData()));
    }

    public void recordContactWithPresent(Long userId) {
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.CONTACT_WITH_PRESENT, null));
    }

    public void recordEmotionNote(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.EMOTION_NOTE, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.EMOTION_NOTE, actRecordConverter.convertToMap(jsonNode)));
    }

    public void recordCognitiveDefusion(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.COGNITIVE_DEFUSION, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.COGNITIVE_DEFUSION, actRecordConverter.convertToMap(jsonNode)));
    }

    public void recordAcceptance(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.ACCEPTANCE, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.ACCEPTANCE, actRecordConverter.convertToMap(jsonNode)));
    }
}
