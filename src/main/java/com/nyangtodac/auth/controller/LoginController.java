package com.nyangtodac.auth.controller;

import com.nyangtodac.auth.controller.dto.GoogleLoginRequest;
import com.nyangtodac.auth.controller.dto.KakaoLoginRequest;
import com.nyangtodac.auth.controller.dto.TokenResponse;
import com.nyangtodac.auth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest loginRequest) {
        return ResponseEntity.ok(oAuth2Service.googleLogin(loginRequest.getAuthorizationCode(), loginRequest.getCodeVerifier(), loginRequest.getRedirectUri(), loginRequest.getPlatform()));
    }

    @PostMapping("/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest loginRequest) {
        log.info("accessToken from Client: " + loginRequest.getAccessToken());
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(loginRequest.getAccessToken()));
    }
}
