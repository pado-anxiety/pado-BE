package com.nyangtodac.cbt.recommend;

import lombok.Getter;

@Getter
public enum CBT {
    BREATHING("호흡법", 5, 3, 5, 4),
    CALMING_PHRASE("진정 문구", 1, 5, 2, 5),
    GROUNDING("5-4-3-2-1 기법", 4, 5, 5, 3),
    COGNITIVE_REFRAME("인지 재구성", 0, 5, 0, 5);

    private final String koreanName;
    private final int body;
    private final int mind;
    private final int high;
    private final int low;

    CBT(String koreanName, int body, int mind, int high, int low) {
        this.koreanName = koreanName;
        this.body = body;
        this.mind = mind;
        this.high = high;
        this.low = low;
    }
}
