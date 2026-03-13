package com.pado.act.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.ACTType;
import com.pado.act.application.query.ACTRecordItemView;
import com.pado.act.application.query.ACTRecordsView;
import com.pado.act.application.validator.ACTRecordJsonValidator;
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
    public ACTRecordsView findAllActRecords(Long userId, Long cursor) {
        if (cursor == null) {
            cursor = Long.MAX_VALUE;
        }
        List<ACTRecordItemView> itemViews = actRecordRepository.findAllByUserIdAndTsidLessThanOrderByTsidDesc(userId, cursor, PageRequest.of(0, ACT_RECORD_PAGE_SIZE + 1))
                .stream()
                .map(e -> new ACTRecordItemView(e.getTsid(), e.getActType(), null))
                .toList();
        cursor = null;
        if (!itemViews.isEmpty()) cursor = itemViews.get(itemViews.size() - 1).getTsid();
        boolean hasNext = itemViews.size() > ACT_RECORD_PAGE_SIZE;
        if (hasNext) {
            itemViews = itemViews.subList(0, ACT_RECORD_PAGE_SIZE);
        }
        return new ACTRecordsView(itemViews, cursor, hasNext);
    }

    @Transactional(readOnly = true)
    public ACTRecordItemView findACTRecordResponse(Long userId, Long recordId) {
        ACTRecordEntity entity = actRecordRepository.findById(recordId).orElseThrow(() -> new ACTRecordNotFoundException(recordId));
        if (!entity.getUserId().equals(userId)) {
            throw new ACTAccessDeniedException();
        }
        return new ACTRecordItemView(entity.getTsid(), entity.getActType(), actRecordConverter.convertToJsonNode(entity.getData()));
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
