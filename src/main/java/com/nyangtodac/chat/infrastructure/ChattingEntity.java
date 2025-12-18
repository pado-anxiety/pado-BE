package com.nyangtodac.chat.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "chatting")
@Getter
public class ChattingEntity {

    @Id
    private Long tsid;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String jsonPayload;

    protected ChattingEntity() {
    }

    public ChattingEntity(Long tsid, Long userId, String jsonPayload) {
        this.tsid = tsid;
        this.userId = userId;
        this.jsonPayload = jsonPayload;
    }
}
