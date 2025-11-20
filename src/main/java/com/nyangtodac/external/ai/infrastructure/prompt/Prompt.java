package com.nyangtodac.external.ai.infrastructure.prompt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prompt {

    private String system;

    private String user;

    private String assistant;

    public Prompt() {
    }

    public Prompt(String system, String user, String assistant) {
        this.system = system;
        this.user = user;
        this.assistant = assistant;
    }

}
