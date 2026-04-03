package com.pado.quiz.dto;

import lombok.Getter;

@Getter
public class Choice {
    private String label;
    private int score;

    public Choice(String label, int score) {
        this.label = label;
        this.score = score;
    }
}