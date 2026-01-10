package com.nyangtodac.feedback;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String feedback;

    private Feedback(Long id, Long userId, String feedback) {
        this.id = id;
        this.userId = userId;
        this.feedback = feedback;
    }

    public Feedback(Long userId, String feedback) {
        this(null, userId, feedback);
    }

    protected Feedback() {
    }
}
