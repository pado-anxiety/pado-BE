package com.nyangtodac.act.application;

import lombok.Getter;

@Getter
public class ACTRecordNotFoundException extends RuntimeException {
    private final Long recordId;

    public ACTRecordNotFoundException(Long recordId) {
        this.recordId = recordId;
    }
}
