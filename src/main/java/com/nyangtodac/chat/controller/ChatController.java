package com.nyangtodac.chat.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.chat.application.ChatService;
import com.nyangtodac.chat.application.MessageService;
import com.nyangtodac.chat.controller.dto.ChatHistory;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import com.nyangtodac.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(@LoginUser Long id, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(chatService.postMessage(id, request));
    }

    @GetMapping
    public ResponseEntity<ChatHistory> getRecentMessages(@LoginUser Long id) {
        return ResponseEntity.ok(messageService.getRecentMessages(id));
    }

    @GetMapping("/quota")
    public ResponseEntity<QuotaStatus> getQuotaStatus(@LoginUser Long id) {
        return ResponseEntity.ok(chatService.getQuotaStatus(id));
    }
}
