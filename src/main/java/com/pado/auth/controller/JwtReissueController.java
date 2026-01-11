package com.pado.auth.controller;

import com.pado.auth.controller.dto.TokenReissueRequest;
import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.service.JwtReissueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtReissueController {

    private final JwtReissueService jwtReissueService;

    @PostMapping("/tokens/reissue")
    public ResponseEntity<TokenResponse> reissueTokens(@RequestBody TokenReissueRequest request) {
        return ResponseEntity.ok(jwtReissueService.reissue(request.getRefreshToken()));
    }
}
