package com.nyangtodac.cbt.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
import com.nyangtodac.chat.application.CBTService;
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
    public ResponseEntity<CBTRecommendResponse> recommendCBT(@LoginUser Long id, @RequestBody CBTRecommendRequest cbtRecommendRequest) {
        CBTRecommendResponse response = cbtService.recommendCBT(id, cbtRecommendRequest);
        return ResponseEntity.ok(response);
    }

}
