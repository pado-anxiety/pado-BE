package com.nyangtodac.chat.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.chat.application.ChatService;
import com.nyangtodac.chat.application.ChattingService;
import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
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
    private final ChattingService chattingService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(@LoginUser Long id, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(chatService.postMessage(id, request));
    }

    @GetMapping
    public ResponseEntity<ChatMessagesResponse> getRecentChattings(@LoginUser Long id) {
        return ResponseEntity.ok(chattingService.getRecentChattings(id));
    }

    @GetMapping("/quota")
    public ResponseEntity<QuotaStatus> getQuotaStatus(@LoginUser Long id) {
        return ResponseEntity.ok(chatService.getQuotaStatus(id));
    }
}
