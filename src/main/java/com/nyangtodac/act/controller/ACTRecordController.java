package com.nyangtodac.act.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nyangtodac.act.application.ACTRecordService;
import com.nyangtodac.act.controller.dto.ACTRecords;
import com.nyangtodac.auth.infrastructure.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class ACTRecordController {

    private final ACTRecordService actRecordService;

    @GetMapping
    public ResponseEntity<ACTRecords> getACTRecords(@LoginUser Long userId) {
        return ResponseEntity.ok(actRecordService.findAllActRecords(userId));
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
}
