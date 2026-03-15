package com.pado.diary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.diary.application.DiaryService;
import com.pado.diary.controller.dto.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Void> save(@LoginUser AuthUser authUser, @RequestBody JsonNode jsonNode) {
        diaryService.saveDiary(authUser.getUserId(), jsonNode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponse> getDiary(@LoginUser AuthUser authUser, @PathVariable Long id) {
        DiaryResponse response = diaryService.getDiary(authUser.getUserId(), authUser.getZoneId(), id);
        return ResponseEntity.ok(response);
    }
}
