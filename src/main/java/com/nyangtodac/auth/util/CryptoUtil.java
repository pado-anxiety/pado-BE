package com.nyangtodac.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptoUtil {

    private final BytesEncryptor encryptor;
    private final ObjectMapper objectMapper;

    public CryptoUtil(
            @Value("${cookie.encryptor.password}") String password,
            @Value("${cookie.encryptor.salt}") String salt
    ) {
        this.encryptor = Encryptors.stronger(password, salt);
        this.objectMapper = new ObjectMapper();
    }

    public String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
        String json;
        try {
            json = objectMapper.writeValueAsString(authorizationRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] encrypted = encryptor.encrypt(json.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public OAuth2AuthorizationRequest decrypt(Cookie cookie) {
        byte[] encrypted = Base64.getDecoder().decode(cookie.getValue());
        byte[] decrypted = encryptor.decrypt(encrypted);
        try {
            return objectMapper.readValue(new String(decrypted, StandardCharsets.UTF_8), OAuth2AuthorizationRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
