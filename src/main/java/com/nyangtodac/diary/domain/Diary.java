package com.nyangtodac.diary.domain;

import lombok.Getter;

@Getter
public class Diary {

    public static final int MAX_CONTENT_LENGTH = 2000;

    private final Long id;
    private final Long userId;

    private int anxietyLevel;
    private AnxietySituation anxietySituation;
    private String content;

    private Visibility visibility;

    public Diary(Long id, Long userId, int anxietyLevel, AnxietySituation anxietySituation, String content, Visibility visibility) {
        this.id = id;
        this.userId = userId;
        this.anxietyLevel = anxietyLevel;
        this.anxietySituation = anxietySituation;
        this.content = content;
        this.visibility = visibility;
        validate();
    }

    public void changeAnxietyLevel(int anxietyLevel) {
        this.anxietyLevel = anxietyLevel;
        validate();
    }

    public void changeAnxietySituation(AnxietySituation anxietySituation) {
        this.anxietySituation = anxietySituation;
        validate();
    }

    public void changeContent(String content) {
        this.content = content;
        validate();
    }


    public void changeVisibility(Visibility visibility) {
        this.visibility = visibility;
        validate();
    }

    private void validate() {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content는 비어있을 수 없습니다.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("content는 " + MAX_CONTENT_LENGTH + "자 이하여야 합니다.");
        }
        if (anxietyLevel < 1 || anxietyLevel > 10) {
            throw new IllegalArgumentException("anxietyLevel은 1~10 사이여야 합니다.");
        }
        if (anxietySituation == null) {
            throw new IllegalArgumentException("anxietySituation은 null일 수 없습니다.");
        }
        if (visibility == null) {
            throw new IllegalArgumentException("visibility는 null일 수 없습니다.");
        }
    }
}
