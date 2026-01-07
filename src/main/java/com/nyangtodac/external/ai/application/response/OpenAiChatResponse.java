package com.nyangtodac.external.ai.application.response;

import lombok.Getter;

@Getter
public class OpenAiChatResponse {

    private final String reply;

    public OpenAiChatResponse(String reply) {
        this.reply = reply;
    }
}
