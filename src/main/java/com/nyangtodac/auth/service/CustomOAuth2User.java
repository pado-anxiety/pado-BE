package com.nyangtodac.auth.service;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String email;

    public CustomOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
        super(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2UserInfo.getAttributes(),
                oAuth2UserInfo.getNameAttributeKey()
        );
        this.email = oAuth2UserInfo.getEmail();
    }
}
