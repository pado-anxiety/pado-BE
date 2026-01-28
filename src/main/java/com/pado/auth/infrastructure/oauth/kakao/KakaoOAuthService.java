package com.pado.auth.infrastructure.oauth.kakao;

import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.jwt.JwtTokenProvider;
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
public class KakaoOAuthService {

    private final KakaoIdentityTokenVerifier identityTokenVerifier;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenResponse kakaoLogin(String identityToken) {
        KakaoClaims claims = identityTokenVerifier.verify(identityToken);
        Optional<User> optional = userRepository.findBySubAndLoginType(claims.getSub(), LoginType.KAKAO);
        User user = optional.orElseGet(() -> userRepository.save(new User(claims.getEmail(), claims.getSub(), claims.getName(), LoginType.KAKAO)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }
}
