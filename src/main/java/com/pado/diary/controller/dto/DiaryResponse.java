package com.pado.diary.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DiaryResponse {
    private final LocalDateTime time;
    private final String situation;
    private final String thoughts;
    private final List<String> feelings;

    public DiaryResponse(LocalDateTime time, String situation, String thoughts, List<String> feelings) {
        this.time = time;
        this.situation = situation;
        this.thoughts = thoughts;
        this.feelings = feelings;
    }
}
