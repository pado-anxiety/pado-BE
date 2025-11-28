package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.controller.dto.Sender;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "message")
@Getter
public class MessageEntity {

    @Id
    private Long tsid;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sender sender;

    @Column(nullable = false)
    private String content;

    protected MessageEntity() {
    }

    public MessageEntity(Long tsid, Long userId, Sender sender, String content) {
        this.tsid = tsid;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
    }
}
