package com.pado.quiz.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Question {
    private int id;
    private String category;
    private String situation;
    private List<Choice> choices;

    public Question(int id, String category, String situation, List<Choice> choices) {
        this.id = id;
        this.category = category;
        this.situation = situation;
        this.choices = choices;
    }

}