package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.controller.dto.Sender;
import com.nyangtodac.chat.domain.Chatting;
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

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sender sender;

    protected ChattingEntity() {
    }

    public static ChattingEntity fromModel(Long userId, Chatting chatting) {
        ChattingEntity entity = new ChattingEntity();
        entity.tsid = chatting.getTsid();
        entity.userId = userId;
        entity.message = chatting.getMessage();
        entity.sender = Sender.valueOf(chatting.getSender());
        return entity;
    }

    public Chatting toModel() {
        return new Chatting(tsid, message, sender);
    }
}
