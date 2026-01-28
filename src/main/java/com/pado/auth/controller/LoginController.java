package com.pado.auth.controller;

import com.pado.auth.controller.dto.AppleLoginRequest;
import com.pado.auth.controller.dto.GoogleLoginRequest;
import com.pado.auth.controller.dto.KakaoLoginRequest;
import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.auth.infrastructure.oauth.apple.AppleOAuthService;
import com.pado.auth.infrastructure.oauth.google.GoogleOAuthService;
import com.pado.auth.infrastructure.oauth.kakao.KakaoOAuthService;
import com.pado.auth.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final GoogleOAuthService googleOAuthService;
    private final KakaoOAuthService kakaoOAuthService;
    private final AppleOAuthService appleOAuthService;

    private final LogoutService logoutService;

    @PostMapping("/login/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest loginRequest) {
        return ResponseEntity.ok(googleOAuthService.googleLogin(loginRequest.getAuthorizationCode(), loginRequest.getCodeVerifier(), loginRequest.getRedirectUri(), loginRequest.getPlatform()));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest loginRequest) {
        return ResponseEntity.ok(kakaoOAuthService.kakaoLogin(loginRequest.getIdentityToken(), loginRequest.getRefreshToken()));
    }

    @PostMapping("/login/apple")
    public ResponseEntity<TokenResponse> appleLogin(@RequestBody AppleLoginRequest loginRequest) {
        return ResponseEntity.ok(appleOAuthService.appleLogin(loginRequest.getAuthorizationCode(), loginRequest.getFullName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@LoginUser Long userId) {
        logoutService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
