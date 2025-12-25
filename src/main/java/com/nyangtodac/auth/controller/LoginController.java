package com.nyangtodac.auth.controller;

import com.nyangtodac.auth.controller.dto.LoginRequest;
import com.nyangtodac.auth.controller.dto.TokenResponse;
import com.nyangtodac.auth.service.OAuth2Service;
import com.nyangtodac.user.domain.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(oAuth2Service.login(loginRequest.getAuthorizationCode(), loginRequest.getCodeVerifier(), LoginType.GOOGLE));
    }
}
