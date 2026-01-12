package com.pado.act.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.ACTType;
import com.pado.act.controller.dto.ACTRecordResponse;
import com.pado.act.controller.dto.ACTRecords;
import com.pado.act.infrastructure.ACTRecordEntity;
import com.pado.act.infrastructure.ACTRecordRepository;
import com.pado.tsid.ACTRecordTsidUtil;
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
    public ACTRecords findAllActRecords(Long userId, String stringCursor) {
        long cursor = Long.MAX_VALUE;
        if (stringCursor != null) {
            cursor = Long.parseLong(stringCursor);
        }
        List<ACTRecordEntity> entities = actRecordRepository.findAllByUserIdAndTsidLessThanOrderByTsidDesc(userId, cursor, PageRequest.of(0, ACT_RECORD_PAGE_SIZE + 1)); // hasNext를 위한 +1 조회
        return ACTRecordMapper.toACTRecords(entities, ACT_RECORD_PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    public ACTRecordResponse findACTRecordResponse(Long userId, Long recordId) {
        ACTRecordEntity entity = actRecordRepository.findById(recordId).orElseThrow(() -> new ACTRecordNotFoundException(recordId));
        if (!entity.getUserId().equals(userId)) {
            throw new ACTAccessDeniedException();
        }
        return new ACTRecordResponse(entity.getTsid(), ACTRecordTsidUtil.toLocalDateTime(entity.getTsid()), entity.getActType(), actRecordConverter.convertToJsonNode(entity.getData()));
    }

    public void recordContactWithPresent(Long userId) {
        actRecordRepository.save(new ACTRecordEntity(generatedTsid(), userId, ACTType.CONTACT_WITH_PRESENT, null));
    }

    public void recordEmotionNote(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.EMOTION_NOTE, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(generatedTsid(), userId, ACTType.EMOTION_NOTE, actRecordConverter.convertToMap(jsonNode)));
    }

    public void recordCognitiveDefusion(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.COGNITIVE_DEFUSION, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(generatedTsid(), userId, ACTType.COGNITIVE_DEFUSION, actRecordConverter.convertToMap(jsonNode)));
    }

    public void recordAcceptance(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.ACCEPTANCE, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(generatedTsid(), userId, ACTType.ACCEPTANCE, actRecordConverter.convertToMap(jsonNode)));
    }

    public void recordCommittedAction(Long userId, JsonNode jsonNode) {
        jsonValidator.validate(ACTType.COMMITTED_ACTION, jsonNode);
        actRecordRepository.save(new ACTRecordEntity(generatedTsid(), userId, ACTType.COMMITTED_ACTION, actRecordConverter.convertToMap(jsonNode)));
    }

    private Long generatedTsid() {
        return ACTRecordTsidUtil.generate();
    }
}
