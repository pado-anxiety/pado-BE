package com.pado.act.controller;

import com.pado.act.recommend.ACTRecommendService;
import com.pado.act.recommend.ACTRecommendation;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ACTRecommendController {

    private final ACTRecommendService actRecommendService;

    @PostMapping("/act/recommend")
    public ResponseEntity<ACTRecommendation> recommend(@LoginUser AuthUser authUser) {
        return ResponseEntity.ok(actRecommendService.recommend(authUser.getUserId(), authUser.getZoneId()));
    }
}
