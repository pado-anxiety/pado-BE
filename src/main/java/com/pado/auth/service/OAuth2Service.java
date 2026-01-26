package com.pado.auth.service;

import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.jwt.JwtTokenProvider;
import com.pado.auth.infrastructure.oauth.Platform;
import com.pado.auth.infrastructure.oauth.UserInfo;
import com.pado.auth.infrastructure.oauth.apple.AppleClaims;
import com.pado.auth.infrastructure.oauth.apple.AppleIdentityTokenVerifier;
import com.pado.auth.infrastructure.oauth.apple.AppleLoginClient;
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
    private final AppleLoginClient appleLoginClient;

    private final AppleIdentityTokenVerifier appleIdentityTokenVerifier;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse googleLogin(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        String accessToken = googleLoginClient.getAccessToken(authorizationCode, codeVerifier, redirectUri, platform);
        UserInfo userInfo = googleLoginClient.getUserInfo(accessToken);

        //FIXME email -> sub
        Optional<User> optional = userRepository.findBySubAndLoginType(userInfo.getEmail(), LoginType.GOOGLE);
        //FIXME
        User user = optional.orElseGet(() -> userRepository.save(new User(userInfo.getEmail(), null, userInfo.getName(), LoginType.GOOGLE)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }

    public TokenResponse kakaoLogin(String accessToken) {
        UserInfo userInfo = kakaoLoginClient.getUserInfo(accessToken);

        //FIXME email -> sub
        Optional<User> optional = userRepository.findBySubAndLoginType(userInfo.getEmail(), LoginType.KAKAO);
        //FIXME
        User user = optional.orElseGet(() -> userRepository.save(new User(userInfo.getEmail(), null, userInfo.getName(), LoginType.KAKAO)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }

    public TokenResponse appleLogin(String authorizationCode, String fullName) {
        String idToken = appleLoginClient.getToken(authorizationCode);
        AppleClaims claims = appleIdentityTokenVerifier.verify(idToken);

        User user;
        Optional<User> getUser = userRepository.findBySubAndLoginType(claims.getSub(), LoginType.APPLE);
        if (getUser.isEmpty()) {
            user = userRepository.save(new User(claims.getEmail(), claims.getSub(), fullName, LoginType.APPLE));
        } else {
            user = getUser.get();
            if (!user.getEmail().equals(claims.getEmail())) {
                user.updateEmail(claims.getEmail());
                userRepository.save(user);
            }
        }

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), jwtTokenProvider.createRefreshToken(user.getId()));
    }

    public void logout(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.updateRefreshToken(null);
        userRepository.save(user);
    }
}
