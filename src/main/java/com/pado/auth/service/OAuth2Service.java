package com.pado.auth.service;

import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.jwt.JwtTokenProvider;
import com.pado.auth.infrastructure.oauth.Platform;
import com.pado.auth.infrastructure.oauth.UserInfo;
import com.pado.auth.infrastructure.oauth.google.GoogleLoginClient;
import com.pado.auth.infrastructure.oauth.kakao.KakaoLoginClient;
import com.pado.user.application.UserNotFoundException;
import com.pado.user.application.UserRepository;
import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2Service {

    private final GoogleLoginClient googleLoginClient;
    private final KakaoLoginClient kakaoLoginClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse googleLogin(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        String accessToken = googleLoginClient.getAccessToken(authorizationCode, codeVerifier, redirectUri, platform);
        UserInfo userInfo = googleLoginClient.getUserInfo(accessToken);

        Optional<User> optional = userRepository.findByEmailAndLoginType(userInfo.getEmail(), LoginType.GOOGLE);
        User user = optional.orElseGet(() -> userRepository.save(new User(userInfo.getEmail(), userInfo.getName(), LoginType.GOOGLE)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }

    public TokenResponse kakaoLogin(String accessToken) {
        UserInfo userInfo = kakaoLoginClient.getUserInfo(accessToken);

        Optional<User> optional = userRepository.findByEmailAndLoginType(userInfo.getEmail(), LoginType.KAKAO);
        User user = optional.orElseGet(() -> userRepository.save(new User(userInfo.getEmail(), userInfo.getName(), LoginType.KAKAO)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }

    public void logout(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.updateRefreshToken(null);
        userRepository.save(user);
    }
}
