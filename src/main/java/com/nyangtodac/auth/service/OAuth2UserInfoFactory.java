package com.nyangtodac.auth.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class OAuth2UserInfoFactory {

    private static final Map<String, Supplier<OAuth2UserInfo>> REGISTRY = new HashMap<>();

    public OAuth2UserInfoFactory() {
        REGISTRY.put("kakao", KakaoOAuth2UserInfo::new);
        REGISTRY.put("google", GoogleOAuth2UserInfo::new);
    }

    public OAuth2UserInfo create(String provider, String nameAttributeName, Map<String, Object> attributes) {
        Supplier<OAuth2UserInfo> creator = REGISTRY.get(provider.toLowerCase());
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        OAuth2UserInfo oAuth2UserInfo = creator.get();
        oAuth2UserInfo.fill(nameAttributeName, attributes);
        return oAuth2UserInfo;
    }

}
