package com.nyangtodac.cbt.domain;

import lombok.Getter;

@Getter
public class CalmingPhrase implements CBT {

    private final int viewCount;

    public CalmingPhrase(int viewCount) {
        this.viewCount = viewCount;
    }
}
