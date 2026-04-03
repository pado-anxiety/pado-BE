package com.pado.quiz.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "quiz_gad")
public class GADEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    protected GADEntity() {
    }

    public GADEntity(Integer score) {
        this.score = score;
    }
}
