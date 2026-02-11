package com.pado.act.recommend.infrastructure;

import com.pado.act.ACTType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "ai_act_recommend")
public class AiACTRecommendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Instant time;

    @Enumerated(EnumType.STRING)
    private ACTType actType;

    protected AiACTRecommendEntity() {
    }

    public AiACTRecommendEntity(Long userId, Instant time, ACTType actType) {
        this.userId = userId;
        this.time = time;
        this.actType = actType;
    }
}
