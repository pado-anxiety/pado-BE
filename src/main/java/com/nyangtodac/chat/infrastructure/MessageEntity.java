package com.nyangtodac.chat.infrastructure;

import com.nyangtodac.chat.controller.dto.Sender;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sender sender;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected MessageEntity() {
    }

    public MessageEntity(Long id, Long userId, Sender sender, String content) {
        this.id = id;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public MessageEntity(Long userId, Sender sender, String content) {
        this(null, userId, sender, content);
    }
}
