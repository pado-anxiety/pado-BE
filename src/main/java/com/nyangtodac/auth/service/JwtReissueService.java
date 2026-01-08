package com.nyangtodac.auth.service;

import com.nyangtodac.auth.infrastructure.jwt.JwtTokenProvider;
import com.nyangtodac.auth.controller.dto.TokenResponse;
import com.nyangtodac.user.application.UserNotFoundException;
import com.nyangtodac.user.application.UserRepository;
import com.nyangtodac.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenResponse reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        TokenResponse tokenResponse = jwtTokenProvider.reissueTokens(user, refreshToken);
        user.updateRefreshToken(tokenResponse.getRefreshToken());
        userRepository.save(user);
        return tokenResponse;
    }
}
