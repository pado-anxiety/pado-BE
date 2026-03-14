package com.pado.act.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.application.ACTRecordService;
import com.pado.act.application.InvalidActRecordRequestException;
import com.pado.act.application.query.ACTRecordItemView;
import com.pado.act.application.query.ACTRecordsView;
import com.pado.act.controller.dto.ACTRecordResponse;
import com.pado.act.controller.dto.ACTRecords;
import com.pado.act.controller.mapper.ACTRecordMapper;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.tsid.ACTRecordTsidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class ACTRecordController {

    private final ACTRecordService actRecordService;

    @GetMapping
    public ResponseEntity<ACTRecords> getACTRecords(@LoginUser AuthUser authUser, @RequestParam(required = false) String cursor) {
        Long parsedCursor = null;
        try {
            if (cursor != null) {
                parsedCursor = Long.parseLong(cursor);
            }
        } catch (NumberFormatException e) {
            throw new InvalidActRecordRequestException("cursor 값이 올바르지 않습니다.");
        }
        ACTRecordsView view = actRecordService.findAllActRecords(authUser.getUserId(), parsedCursor);
        return ResponseEntity.ok(ACTRecordMapper.toACTRecords(view, authUser.getZoneId()));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ACTRecordResponse> getACTRecordResponse(@LoginUser AuthUser authUser, @PathVariable String recordId) {
        long parsedRecordId;
        try {
            parsedRecordId = Long.parseLong(recordId);
        } catch (NumberFormatException e) {
            throw new InvalidActRecordRequestException("recordId 값이 올바르지 않습니다.");
        }
        ACTRecordItemView view = actRecordService.findACTRecordResponse(authUser.getUserId(), parsedRecordId);
        return ResponseEntity.ok(new ACTRecordResponse(view.getTsid(), ACTRecordTsidUtil.toLocalDateTime(view.getTsid(), authUser.getZoneId()), view.getActType(), view.getData()));
    }

    @PostMapping("/contact-with-present")
    public ResponseEntity<Void> contactWithPresent(@LoginUser AuthUser authUser) {
        actRecordService.recordContactWithPresent(authUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cognitive-defusion")
    public ResponseEntity<Void> cognitiveDefusion(@LoginUser AuthUser authUser, @RequestBody JsonNode data) {
        actRecordService.recordCognitiveDefusion(authUser.getUserId(), data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/acceptance")
    public ResponseEntity<Void> acceptance(@LoginUser AuthUser authUser, @RequestBody JsonNode data) {
        actRecordService.recordAcceptance(authUser.getUserId(), data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/committed-action")
    public ResponseEntity<Void> committedAction(@LoginUser AuthUser authUser, @RequestBody JsonNode data) {
        actRecordService.recordCommittedAction(authUser.getUserId(), data);
        return ResponseEntity.ok().build();
    }
}
