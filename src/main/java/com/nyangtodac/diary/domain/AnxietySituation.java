package com.nyangtodac.diary.domain;

import lombok.Getter;

@Getter
public enum AnxietySituation {

    SOCIAL("사회적 상황"),
    WORK_STUDY("업무/학업"),
    HEALTH("건강"),
    RELATIONSHIPS("인간관계"),
    FINANCIAL("경제적 문제"),
    FUTURE("미래"),
    DAILY_LIFE("일상생활"),
    OTHER("기타");

    private final String label;

    AnxietySituation(String label) {
        this.label = label;
    }

}
