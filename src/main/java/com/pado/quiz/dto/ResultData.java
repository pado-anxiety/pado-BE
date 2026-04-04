package com.pado.quiz.dto;

import lombok.Getter;

@Getter
public class ResultData {
    private String level;
    private String range;
    private String title;
    private String description;
    private String message;

    public ResultData(String level, String range, String title, String description, String message) {
        this.level = level;
        this.range = range;
        this.title = title;
        this.description = description;
        this.message = message;
    }
}