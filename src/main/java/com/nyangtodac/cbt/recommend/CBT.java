package com.nyangtodac.cbt.recommend;

import lombok.Getter;

@Getter
public enum CBT {
    BREATHING(5, 3, 5, 4),
    CALMING_PHRASE(4, 5, 5, 3),
    GROUNDING(1, 5, 2, 5),
    COGNITIVE_REFRAME(0, 5, 0, 5);

    private final int body;
    private final int mind;
    private final int high;
    private final int low;

    CBT(int body, int mind, int high, int low) {
        this.body = body;
        this.mind = mind;
        this.high = high;
        this.low = low;
    }
}
