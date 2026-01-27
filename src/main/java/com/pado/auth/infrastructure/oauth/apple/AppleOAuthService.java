package com.pado.auth.infrastructure.oauth.apple;

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
public class AppleOAuthService {

    private final AppleLoginClient loginClient;
    private final AppleIdentityTokenVerifier identityTokenVerifier;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenResponse appleLogin(String authorizationCode, String fullName) {
        String idToken = loginClient.getToken(authorizationCode);
        AppleClaims claims = identityTokenVerifier.verify(idToken);

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
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        user = userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), refreshToken);
    }
}
