package com.pado.external.ai.application.response;

import lombok.Getter;

@Getter
public class OpenAiSummaryResponse {

    private final String summaryText;

    public OpenAiSummaryResponse(String summaryText) {
        this.summaryText = summaryText;
    }
}
