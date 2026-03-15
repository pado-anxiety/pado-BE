package com.pado.diary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.diary.application.DiaryService;
import com.pado.diary.controller.dto.DiaryCalendarItem;
import com.pado.diary.controller.dto.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
        return ResponseEntity.ok(diaryService.getDiary(authUser.getUserId(), authUser.getZoneId(), id));
    }

    @GetMapping
    public ResponseEntity<List<DiaryCalendarItem>> getCalendarItems(
            @LoginUser AuthUser authUser,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        if (year == null || month == null) {
            LocalDate localDate = LocalDate.ofInstant(Instant.now(), authUser.getZoneId());
            year = localDate.getYear();
            month = localDate.getMonthValue();
        }

        return ResponseEntity.ok(diaryService.getCalendarItems(authUser.getUserId(), authUser.getZoneId(), year, month));
    }
}
