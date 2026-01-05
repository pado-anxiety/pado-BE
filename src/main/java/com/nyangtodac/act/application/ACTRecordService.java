package com.nyangtodac.act.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.nyangtodac.act.ACTType;
import com.nyangtodac.act.controller.dto.ACTRecordResponse;
import com.nyangtodac.act.controller.dto.ACTRecords;
import com.nyangtodac.act.infrastructure.ACTRecordEntity;
import com.nyangtodac.act.infrastructure.ACTRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ACTRecordService {

    private final ACTRecordRepository actRecordRepository;
    private final ACTRecordJsonValidator jsonValidator;

    @Transactional(readOnly = true)
    public ACTRecords findAllActRecords(Long userId) {
        return ACTRecordMapper.toACTRecords(actRecordRepository.findAllByUserIdOrderByTimeDesc(userId));
    }

    @Transactional(readOnly = true)
    public ACTRecordResponse findACTRecordResponse(Long userId, Long recordId) {
        ACTRecordEntity entity = actRecordRepository.findById(recordId).orElseThrow(() -> new ACTRecordNotFoundException(recordId));
        if (!entity.getUserId().equals(userId)) {
            throw new ACTAccessDeniedException();
        }
        return new ACTRecordResponse(entity.getId(), entity.getTime(), entity.getActType(), entity.getData());
    }

    public void recordContactWithPresent(Long userId) {
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.CONTACT_WITH_PRESENT, null));
    }

    public void recordEmotionNote(Long userId, JsonNode data) {
        jsonValidator.validate(ACTType.EMOTION_NOTE, data);
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.EMOTION_NOTE, data));
    }

    public void recordCognitiveDefusion(Long userId, JsonNode data) {
        jsonValidator.validate(ACTType.COGNITIVE_DEFUSION, data);
        actRecordRepository.save(new ACTRecordEntity(userId, ACTType.COGNITIVE_DEFUSION, data));
    }
}
