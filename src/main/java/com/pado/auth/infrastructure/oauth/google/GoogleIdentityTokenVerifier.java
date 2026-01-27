package com.pado.auth.infrastructure.oauth.google;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public class GoogleIdentityTokenVerifier {

    private static final String URL = "https://www.googleapis.com/oauth2/v3/certs";
    private final List<String> clientIds;

    public GoogleIdentityTokenVerifier(
            @Value("${oauth2.client.registration.google.android.client-id}") String androidClientId,
            @Value("${oauth2.client.registration.google.ios.client-id}") String iosClientId
    ) {
        this.clientIds = List.of(androidClientId, iosClientId);
    }

    public GoogleClaims verify(String idToken) {
        try {
            JWKSet jwkSet = JWKSet.load(new URL(URL));
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
            RSAKey rsaKey = (RSAKey) jwk;
            RSASSAVerifier verifier = new RSASSAVerifier(rsaKey);
            if (!signedJWT.verify(verifier)) {
                throw new SecurityException("Google ID Token 서명 검증 실패");
            }
            JWTClaimsSet claims = validateClaims(signedJWT);
            return new GoogleClaims(claims.getSubject(), claims.getStringClaim("email"), claims.getStringClaim("name"));
        } catch (IOException | ParseException | JOSEException e) {
            throw new RuntimeException("Google ID Token 검증 실패", e);
        }
    }

    private @NotNull JWTClaimsSet validateClaims(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        if (!("https://accounts.google.com".equals(claims.getIssuer())
           || "accounts.google.com".equals(claims.getIssuer()))) {
            throw new SecurityException("Invalid issuer");
        }

        boolean validAudience = claims.getAudience().stream().anyMatch(clientIds::contains);
        if (!validAudience) {
            throw new SecurityException("Invalid audience");
        }

        if (claims.getExpirationTime().before(new Date())) {
            throw new SecurityException("Token expired");
        }

        return claims;
    }
}
