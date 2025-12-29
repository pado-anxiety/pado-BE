package com.nyangtodac.act.domain;

import lombok.Getter;

@Getter
public class CalmingPhrase {

    private final int viewCount;

    public CalmingPhrase(int viewCount) {
        this.viewCount = viewCount;
    }
}
