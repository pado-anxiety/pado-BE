package com.nyangtodac.auth.service;

import com.nyangtodac.user.application.UserRepository;
import com.nyangtodac.user.domain.LoginType;
import com.nyangtodac.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo userInfo = oAuth2UserInfoFactory.create(registrationId, nameAttributeName, oAuth2User.getAttributes());

        saveOrUpdate(userInfo.getEmail(), LoginType.valueOf(registrationId.toUpperCase()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userInfo.getAttributes(),
                userInfo.getNameAttributeKey()
        );
    }

    private void saveOrUpdate(String email, LoginType loginType) {
        User user = userRepository.findByEmailAndLoginType(email, loginType)
                .orElse(new User(email, loginType));

        userRepository.save(user);
    }
}