package com.nyangtodac.cbt.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.chat.application.CBTService;
import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CBTRecommendController {

    private final CBTService cbtService;

    @PostMapping("/cbt/recommend")
    public ResponseEntity<ChatMessagesResponse> recommendCBT(@LoginUser Long id, @RequestBody CBTRecommendRequest cbtRecommendRequest) {
        return ResponseEntity.ok(cbtService.recommendCBT(id, cbtRecommendRequest));
    }

}
