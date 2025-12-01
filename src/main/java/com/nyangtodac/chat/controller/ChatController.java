package com.nyangtodac.chat.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.chat.application.AIChatService;
import com.nyangtodac.chat.application.MessageService;
import com.nyangtodac.chat.controller.dto.ChatHistory;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final AIChatService AIChatService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(@LoginUser Long id, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(AIChatService.postMessage(id, request));
    }

    @GetMapping
    public ResponseEntity<ChatHistory> getRecentMessages(@LoginUser Long id) {
        return ResponseEntity.ok(messageService.getRecentMessages(id));
    }
}
