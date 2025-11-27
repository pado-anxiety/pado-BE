package com.nyangtodac.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String content;

    public ErrorResponse(String content) {
        this.content = content;
    }
}
