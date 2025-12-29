package com.nyangtodac.act.controller;

import com.nyangtodac.act.recommend.ACTRecommendService;
import com.nyangtodac.act.recommend.ACTRecommendation;
import com.nyangtodac.auth.infrastructure.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ACTRecommendController {

    private final ACTRecommendService actRecommendService;

    @PostMapping("/act/recommend")
    public ResponseEntity<ACTRecommendation> recommend(@LoginUser Long userId) {
        return ResponseEntity.ok(actRecommendService.recommend(userId));
    }
}
