package com.nyangtodac.cbt.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
import com.nyangtodac.chat.application.CBTService;
import com.nyangtodac.chat.controller.dto.ChatMessagesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CBTRecommendController {

    private final CBTService cbtService;
    private static final String CBT_RECOMMENDATION_HEADER = "Cbt-Recommendation";

    @PostMapping("/cbt/recommend")
    public ResponseEntity<ChatMessagesResponse> recommendCBT(@LoginUser Long id, @RequestBody CBTRecommendRequest cbtRecommendRequest) {
        CBTRecommendResponse response = cbtService.recommendCBT(id, cbtRecommendRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add(CBT_RECOMMENDATION_HEADER, response.getCbt().name());
        return ResponseEntity.ok().headers(headers).body(response.getChatMessagesResponse());
    }

}
