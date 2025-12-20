package com.nyangtodac.auth.service;

import com.nyangtodac.auth.infrastructure.jwt.JwtTokenProvider;
import com.nyangtodac.auth.controller.dto.TokenResponse;
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

    public TokenResponse reissue(Long userId, String refreshToken) {
        User user = userRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
        return jwtTokenProvider.reissueTokens(user, refreshToken);
    }
}
