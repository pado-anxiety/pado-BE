package com.pado.feedback;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String feedback;

    private FeedbackEntity(Long id, Long userId, String feedback) {
        this.id = id;
        this.userId = userId;
        this.feedback = feedback;
    }

    public FeedbackEntity(Long userId, String feedback) {
        this(null, userId, feedback);
    }

    protected FeedbackEntity() {
    }
}
