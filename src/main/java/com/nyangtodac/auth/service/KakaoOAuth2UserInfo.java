package com.nyangtodac.auth.service;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    protected KakaoOAuth2UserInfo() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void fill(String nameAttributeName, Map<String, Object> attributes) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeName;
        this.email = ((Map<String, Object>) attributes.get("kakao_account")).get("email").toString();
    }
}
