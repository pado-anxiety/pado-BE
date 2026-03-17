package com.pado.diary.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DiaryCalendarItem {

    private final Long id;
    private final LocalDateTime time;
    private final List<String> feelings;

    public DiaryCalendarItem(Long id, LocalDateTime time, List<String> feelings) {
        this.id = id;
        this.time = time;
        this.feelings = feelings;
    }
}
