package com.nyangtodac.external.ai.application.response;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiChatResponse {

    private final List<String> replies;

    public OpenAiChatResponse(List<String> replies) {
        this.replies = replies;
    }
}
