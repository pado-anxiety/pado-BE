package com.nyangtodac.act.recommend.infrastructure;

import com.nyangtodac.act.ACTType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_act_recommend")
public class AiACTRecommendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private ACTType actType;

    protected AiACTRecommendEntity() {
    }

    public AiACTRecommendEntity(Long userId, LocalDateTime time, ACTType actType) {
        this.userId = userId;
        this.time = time;
        this.actType = actType;
    }
}
