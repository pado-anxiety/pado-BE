package com.nyangtodac.cbt.domain;

import lombok.Getter;

@Getter
public class CognitiveReframe implements CBT {

    private final String situation;
    private final String automaticThought;
    private final String emotion;
    private final String adaptiveResponse;
    private final int improvementLevel;

    public CognitiveReframe(String situation, String automaticThought, String emotion, String adaptiveResponse, int improvementLevel) {
        this.situation = situation;
        this.automaticThought = automaticThought;
        this.emotion = emotion;
        this.adaptiveResponse = adaptiveResponse;
        this.improvementLevel = improvementLevel;
    }
}
