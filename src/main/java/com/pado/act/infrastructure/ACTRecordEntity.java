package com.pado.act.infrastructure;

import com.pado.act.ACTType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "act_record")
@Getter
public class ACTRecordEntity {

    @Id
    private Long tsid;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ACTType actType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> data;

    protected ACTRecordEntity() {
    }

    public ACTRecordEntity(Long tsid, Long userId, ACTType actType, Map<String, Object> data) {
        this.tsid = tsid;
        this.userId = userId;
        this.actType = actType;
        this.data = data;
    }
}
