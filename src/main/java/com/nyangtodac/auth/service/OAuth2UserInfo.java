package com.nyangtodac.auth.service;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    protected String nameAttributeKey;
    protected String email;

    protected OAuth2UserInfo() {
    }

    protected abstract void fill(String nameAttributeKey, Map<String, Object> attributes);
}
