package com.pado.act.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.application.ACTRecordService;
import com.pado.act.controller.dto.ACTRecordResponse;
import com.pado.act.controller.dto.ACTRecords;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
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
        return ResponseEntity.ok(actRecordService.findAllActRecords(authUser.getUserId(), authUser.getZoneId(), cursor));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ACTRecordResponse> getACTRecordResponse(@LoginUser AuthUser authUser, @PathVariable("recordId") String recordId) {
        return ResponseEntity.ok(actRecordService.findACTRecordResponse(authUser.getUserId(), authUser.getZoneId(), recordId));
    }

    @PostMapping("/contact-with-present")
    public ResponseEntity<Void> contactWithPresent(@LoginUser AuthUser authUser) {
        actRecordService.recordContactWithPresent(authUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/emotion-note")
    public ResponseEntity<Void> emotionNote(@LoginUser AuthUser authUser, @RequestBody JsonNode data) {
        actRecordService.recordEmotionNote(authUser.getUserId(), data);
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
