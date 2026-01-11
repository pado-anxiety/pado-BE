package com.nyangtodac.external.ai.infrastructure.prompt;

import lombok.Getter;

@Getter
public class SystemPrompt {

    private String system;

    public SystemPrompt() {
    }

    public SystemPrompt(String system) {
        this.system = system;
    }
}
