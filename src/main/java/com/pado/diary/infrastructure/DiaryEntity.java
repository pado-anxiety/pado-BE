package com.pado.diary.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Getter
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;

    protected DiaryEntity() {
    }

    private DiaryEntity(Long id, Long userId, Instant createdAt, Map<String, Object> data) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.data = data;
    }

    public DiaryEntity(Long userId, Map<String, Object> data) {
        this(null, userId, Instant.now(), data);
    }
}
