package com.pado.act.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.act.application.ACTRecordService;
import com.pado.act.controller.dto.ACTRecordResponse;
import com.pado.act.controller.dto.ACTRecords;
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
    public ResponseEntity<ACTRecords> getACTRecords(@LoginUser Long userId, @RequestParam(required = false) String cursor) {
        return ResponseEntity.ok(actRecordService.findAllActRecords(userId, cursor));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ACTRecordResponse> getACTRecordResponse(@LoginUser Long userId, @PathVariable("recordId") String recordId) {
        return ResponseEntity.ok(actRecordService.findACTRecordResponse(userId, recordId));
    }

    @PostMapping("/contact-with-present")
    public ResponseEntity<Void> contactWithPresent(@LoginUser Long userId) {
        actRecordService.recordContactWithPresent(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/emotion-note")
    public ResponseEntity<Void> emotionNote(@LoginUser Long userId, @RequestBody JsonNode data) {
        actRecordService.recordEmotionNote(userId, data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cognitive-defusion")
    public ResponseEntity<Void> cognitiveDefusion(@LoginUser Long userId, @RequestBody JsonNode data) {
        actRecordService.recordCognitiveDefusion(userId, data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/acceptance")
    public ResponseEntity<Void> acceptance(@LoginUser Long userId, @RequestBody JsonNode data) {
        actRecordService.recordAcceptance(userId, data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/committed-action")
    public ResponseEntity<Void> committedAction(@LoginUser Long userId, @RequestBody JsonNode data) {
        actRecordService.recordCommittedAction(userId, data);
        return ResponseEntity.ok().build();
    }
}
