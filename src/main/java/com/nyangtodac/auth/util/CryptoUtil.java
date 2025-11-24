package com.nyangtodac.auth.util;

import jakarta.servlet.http.Cookie;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class CryptoUtil {

    private final BytesEncryptor encryptor;

    public CryptoUtil(
            @Value("${cookie.encryptor.password}") String password,
            @Value("${cookie.encryptor.salt}") String salt
    ) {
        this.encryptor = Encryptors.stronger(password, salt);
    }

    public String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
        byte[] serialized = SerializationUtils.serialize(authorizationRequest);
        byte[] encrypted = encryptor.encrypt(serialized);
        return Base64.getUrlEncoder().encodeToString(encrypted);
    }

    public OAuth2AuthorizationRequest decrypt(Cookie cookie) {
        byte[] encrypted = Base64.getUrlDecoder().decode(cookie.getValue());
        byte[] decrypted = encryptor.decrypt(encrypted);
        return SerializationUtils.deserialize(decrypted);
    }
}
