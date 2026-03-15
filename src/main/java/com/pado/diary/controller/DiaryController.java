package com.pado.diary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.diary.application.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<Void> save(@LoginUser AuthUser authUser, @RequestBody JsonNode jsonNode) {
        diaryService.saveDiary(authUser.getUserId(), jsonNode);
        return ResponseEntity.ok().build();
    }
}
