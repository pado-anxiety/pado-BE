package com.pado.auth.controller;

import com.pado.auth.controller.dto.AppleLoginRequest;
import com.pado.auth.controller.dto.GoogleLoginRequest;
import com.pado.auth.controller.dto.KakaoLoginRequest;
import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.auth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest loginRequest) {
        return ResponseEntity.ok(oAuth2Service.googleLogin(loginRequest.getAuthorizationCode(), loginRequest.getCodeVerifier(), loginRequest.getRedirectUri(), loginRequest.getPlatform()));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest loginRequest) {
        return ResponseEntity.ok(oAuth2Service.kakaoLogin(loginRequest.getAccessToken()));
    }

    @PostMapping("/login/apple")
    public ResponseEntity<TokenResponse> appleLogin(@RequestBody AppleLoginRequest loginRequest) {
        return ResponseEntity.ok(oAuth2Service.appleLogin(loginRequest.getAuthorizationCode(), loginRequest.getFullName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@LoginUser Long userId) {
        oAuth2Service.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
