package com.nyangtodac.auth.service;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    protected GoogleOAuth2UserInfo() {
        super();
    }

    @Override
    protected void fill(String nameAttributeName, Map<String, Object> attributes) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeName;
        this.email = (String) attributes.get("email");
    }
}
