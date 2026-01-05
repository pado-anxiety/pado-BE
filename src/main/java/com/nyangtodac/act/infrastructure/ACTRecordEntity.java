package com.nyangtodac.act.infrastructure;

import com.nyangtodac.act.ACTType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "act_record")
@Getter
public class ACTRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ACTType actType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;

    protected ACTRecordEntity() {
    }

    private ACTRecordEntity(Long id, Long userId, LocalDateTime time, ACTType actType, Map<String, Object> data) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.actType = actType;
        this.data = data;
    }

    public ACTRecordEntity(Long userId, ACTType actType, Map<String, Object> data) {
        this(null, userId, LocalDateTime.now(), actType, data);
    }
}
