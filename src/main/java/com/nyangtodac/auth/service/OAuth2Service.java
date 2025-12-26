package com.nyangtodac.auth.service;

import com.nyangtodac.auth.controller.dto.TokenResponse;
import com.nyangtodac.auth.infrastructure.jwt.JwtTokenProvider;
import com.nyangtodac.auth.infrastructure.oauth.OAuth2Client;
import com.nyangtodac.auth.infrastructure.oauth.Platform;
import com.nyangtodac.auth.infrastructure.oauth.UserInfo;
import com.nyangtodac.user.application.UserRepository;
import com.nyangtodac.user.domain.LoginType;
import com.nyangtodac.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class OAuth2Service {

    private final Map<LoginType, OAuth2Client> oauth2ClientStrategySelector;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2Service(List<OAuth2Client> oAuth2Clients, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.oauth2ClientStrategySelector = oAuth2Clients.stream().collect(Collectors.toUnmodifiableMap(OAuth2Client::getType, Function.identity()));
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(String authorizationCode, String codeVerifier, String redirectUri, Platform platform, LoginType loginType) {
        OAuth2Client oAuth2Client = oauth2ClientStrategySelector.get(loginType);
        String accessToken = oAuth2Client.getAccessToken(authorizationCode, codeVerifier, redirectUri, platform);
        UserInfo userInfo = oAuth2Client.getUserInfo(accessToken);

        Optional<User> optional = userRepository.findByEmailAndLoginType(userInfo.getEmail(), loginType);
        User user = optional.orElseGet(() -> userRepository.save(new User(userInfo.getEmail(), loginType)));

        return new TokenResponse(jwtTokenProvider.createAccessToken(user.getId()), jwtTokenProvider.createRefreshToken(user.getId()));
    }

}
