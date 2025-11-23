package com.nyangtodac.auth.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
public abstract class OAuth2UserInfo {

    private static final Map<String, Supplier<OAuth2UserInfo>> REGISTRY = new HashMap<>();

    protected Map<String, Object> attributes;
    protected String nameAttributeKey;
    protected String email;

    protected static void register(String provider, Supplier<OAuth2UserInfo> creator) {
        REGISTRY.put(provider, creator);
    }

    protected OAuth2UserInfo() {
    }

    public static OAuth2UserInfo from(String registrationId, String nameAttributeName, Map<String, Object> attributes) {
        Supplier<OAuth2UserInfo> creator = REGISTRY.get(registrationId.toLowerCase());
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        }

        OAuth2UserInfo oAuth2UserInfo = creator.get();
        oAuth2UserInfo.fill(nameAttributeName, attributes);
        return oAuth2UserInfo;
    }

    protected abstract void fill(String nameAttributeKey, Map<String, Object> attributes);
}
