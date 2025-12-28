package com.nyangtodac.chat.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.chat.application.AIChatFacade;
import com.nyangtodac.chat.application.ChattingService;
import com.nyangtodac.chat.controller.dto.RecentChattingsResponse;
import com.nyangtodac.chat.controller.dto.message.MessageRequest;
import com.nyangtodac.chat.controller.dto.ChattingResponse;
import com.nyangtodac.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final AIChatFacade AIChatFacade;
    private final ChattingService chattingService;

    @PostMapping
    public ResponseEntity<ChattingResponse> send(@LoginUser Long id, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(AIChatFacade.postMessage(id, request));
    }

    @GetMapping
    public ResponseEntity<RecentChattingsResponse> getRecentChattingsWithCursor(
            @LoginUser Long id,
            @RequestParam(name = "cursor", required = false) Long cursor) {
        return ResponseEntity.ok(chattingService.getRecentChattings(id, cursor));
    }

    @GetMapping("/quota")
    public ResponseEntity<QuotaStatus> getQuotaStatus(@LoginUser Long id) {
        return ResponseEntity.ok(AIChatFacade.getQuotaStatus(id));
    }
}
