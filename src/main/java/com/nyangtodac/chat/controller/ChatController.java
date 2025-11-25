package com.nyangtodac.chat.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.chat.application.ChatService;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.message.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(@LoginUser Long id, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(chatService.postMessage(id, request));
    }
}
