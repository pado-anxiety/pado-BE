package com.nyangtodac.cbt.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.cbt.controller.dto.CBTRecommendRequest;
import com.nyangtodac.cbt.controller.dto.CBTRecommendResponse;
import com.nyangtodac.cbt.recommend.CBTRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CBTRecommendController {

    private final CBTRecommendService cbtRecommendService;

    @PostMapping("/cbt/recommend")
    public ResponseEntity<CBTRecommendResponse> recommendCBT(@LoginUser Long id, @RequestBody CBTRecommendRequest cbtRecommendRequest) {
        return ResponseEntity.ok(cbtRecommendService.recommend(id, cbtRecommendRequest));
    }

}
