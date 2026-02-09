package com.pado.chat.controller;

import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.chat.application.AIChatFacade;
import com.pado.chat.application.AIChatQuotaService;
import com.pado.chat.application.ChattingQueryService;
import com.pado.chat.controller.dto.RecentChattingsResponse;
import com.pado.chat.domain.RecentChattings;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.controller.dto.ChattingResponse;
import com.pado.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final AIChatFacade AIChatFacade;
    private final AIChatQuotaService aiChatQuotaService;
    private final ChattingQueryService chattingQueryService;

    @PostMapping
    public ResponseEntity<ChattingResponse> send(@LoginUser AuthUser authUser, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(AIChatFacade.postMessage(authUser.getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<RecentChattingsResponse> getRecentChattingsWithCursor(
            @LoginUser AuthUser authUser,
            @RequestParam(name = "cursor", required = false) Long cursor) {
        RecentChattings recentChattings = chattingQueryService.getRecentChattingsBeforeCursor(authUser.getUserId(), cursor);
        return ResponseEntity.ok(new RecentChattingsResponse(recentChattings.getChattings(), recentChattings.getCursor()));
    }

    @GetMapping("/quota")
    public ResponseEntity<QuotaStatus> getQuotaStatus(@LoginUser AuthUser authUser) {
        return ResponseEntity.ok(aiChatQuotaService.getQuotaStatus(authUser.getUserId()));
    }
}
