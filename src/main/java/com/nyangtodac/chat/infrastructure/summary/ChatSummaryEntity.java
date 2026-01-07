package com.nyangtodac.chat.infrastructure.summary;

import com.nyangtodac.chat.domain.ChatSummary;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_summary")
public class ChatSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long fromTsid;

    @Column(nullable = false)
    private Long toTsid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summaryText;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ChatSummary toModel() {
        return new ChatSummary(fromTsid, toTsid, summaryText);
    }

    public static ChatSummaryEntity fromModel(ChatSummary chatSummary, Long userId) {
        ChatSummaryEntity entity = new ChatSummaryEntity();
        entity.userId = userId;
        entity.fromTsid = chatSummary.getFromTsid();
        entity.toTsid = chatSummary.getToTsid();
        entity.summaryText = chatSummary.getSummaryText();
        entity.createdAt = LocalDateTime.now();
        return entity;
    }

}
