package com.pado.diary.application;

import lombok.Getter;

@Getter
public class DiaryNotFoundException extends RuntimeException {
    private final Long diaryId;

    public DiaryNotFoundException(Long diaryId) {
        this.diaryId = diaryId;
    }
}
