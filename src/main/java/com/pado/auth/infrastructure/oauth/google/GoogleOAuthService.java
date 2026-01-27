package com.pado.auth.infrastructure.oauth.google;

import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.jwt.JwtTokenProvider;
import com.pado.auth.infrastructure.oauth.Platform;
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
public class GoogleOAuthService {

    private final GoogleLoginClient loginClient;
    private final GoogleIdentityTokenVerifier identityTokenVerifier;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenResponse googleLogin(String authorizationCode, String codeVerifier, String redirectUri, Platform platform) {
        GoogleTokenResponse token = loginClient.getToken(authorizationCode, codeVerifier, redirectUri, platform);
        GoogleClaims claims = identityTokenVerifier.verify(token.getIdentityToken());

        Optional<User> optional = userRepository.findBySubAndLoginType(claims.getSub(), LoginType.GOOGLE);
        User user = optional.orElseGet(() -> userRepository.save(new User(claims.getEmail(), claims.getSub(), claims.getName(), LoginType.GOOGLE)));
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }
}
