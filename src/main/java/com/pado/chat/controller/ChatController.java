package com.pado.chat.controller;

import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.chat.application.AIChatFacade;
import com.pado.chat.application.AIChatQuotaService;
import com.pado.chat.application.ChattingQueryService;
import com.pado.chat.application.command.PostMessageResult;
import com.pado.chat.application.query.RecentChattingsView;
import com.pado.chat.controller.dto.ChattingResponse;
import com.pado.chat.controller.dto.RecentChattingsResponse;
import com.pado.chat.controller.dto.message.MessageRequest;
import com.pado.chat.controller.mapper.ChattingResponseMapper;
import com.pado.chat.quota.QuotaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final AIChatFacade AIChatFacade;
    private final AIChatQuotaService aiChatQuotaService;
    private final ChattingQueryService chattingQueryService;

    @PostMapping
    public ResponseEntity<ChattingResponse> send(@LoginUser AuthUser authUser, @RequestBody MessageRequest request) {
        PostMessageResult result = AIChatFacade.postMessage(authUser.getUserId(), request);
        return ResponseEntity.ok(ChattingResponseMapper.from(result.getSender(), result.getMessage(), result.getTsid(), authUser.getZoneId()));
    }

    @GetMapping
    public ResponseEntity<RecentChattingsResponse> getRecentChattingsWithCursor(
            @LoginUser AuthUser authUser,
            @RequestParam(name = "cursor", required = false) Long cursor) {
        RecentChattingsView view = chattingQueryService.getRecentChattingsBeforeCursor(authUser.getUserId(), cursor);
        List<ChattingResponse> chattingResponses = view.getChattings().stream().map(v -> ChattingResponseMapper.from(v.getSender(), v.getMessage(), v.getTsid(), authUser.getZoneId())).toList();
        return ResponseEntity.ok(new RecentChattingsResponse(chattingResponses, view.getCursor()));
    }

    @GetMapping("/quota")
    public ResponseEntity<QuotaStatus> getQuotaStatus(@LoginUser AuthUser authUser) {
        return ResponseEntity.ok(aiChatQuotaService.getQuotaStatus(authUser.getUserId()));
    }
}
