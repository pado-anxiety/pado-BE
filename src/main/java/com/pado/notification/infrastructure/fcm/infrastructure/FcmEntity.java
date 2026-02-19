package com.pado.notification.infrastructure.fcm.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "fcm")
@Getter
public class FcmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String token;

    private Boolean isActive;

    private Instant updatedAt;

    protected FcmEntity() {
    }

    private FcmEntity(Long id, Long userId, String token, Boolean isActive, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.isActive = isActive;
        this.updatedAt = updatedAt;
    }

    public FcmEntity(Long userId, String token) {
        this(null, userId, token, true, Instant.now());
    }

    public void updateIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
