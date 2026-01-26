package com.pado.auth.infrastructure.oauth.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class AppleClientSecretProvider {

    private final String clientId;
    private final String keyId;
    private final String teamId;
    private final PrivateKey privateKey;

    public AppleClientSecretProvider(
            @Value("${oauth2.client.registration.apple.client-id}") String clientId,
            @Value("${oauth2.client.registration.apple.key-id}") String keyId,
            @Value("${oauth2.client.registration.apple.team-id}") String teamId
    ) {
        this.teamId = teamId;
        this.keyId = keyId;
        this.clientId = clientId;
        this.privateKey = loadPrivateKey();
    }

    public String createClientSecretToken() {
        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setSubject(clientId)
                .setIssuer(teamId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .setAudience("https://appleid.apple.com")
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey loadPrivateKey() {
        try {
            String key = System.getenv("APPLE_PRIVATE_KEY")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load Apple private key", e);
        }
    }
}
