package com.pado.auth.infrastructure.oauth.apple;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

@Component
public class AppleIdentityTokenVerifier {

    private static final String URL = "https://appleid.apple.com/auth/keys";

    @Value("${oauth2.client.registration.apple.client-id}")
    private String clientId;

    public AppleClaims verify(String identityToken) {
        try {
            JWKSet jwkSet = JWKSet.load(new URL(URL));
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
            RSAKey rsaKey = (RSAKey) jwk;
            RSASSAVerifier verifier = new RSASSAVerifier(rsaKey);
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("apple identity token 검증에 실패했습니다.");
            }

            JWTClaimsSet claims = getJwtClaimsSet(signedJWT);
            return new AppleClaims(claims.getSubject(), claims.getStringClaim("email"));

        } catch (IOException | ParseException | JOSEException e) {
            throw new RuntimeException("apple identity token 검증 중 오류가 발생했습니다.", e);
        }
    }

    private @NotNull JWTClaimsSet getJwtClaimsSet(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
            throw new SecurityException("Invalid issuer");
        }

        if (!claims.getAudience().contains(clientId)) {
            throw new SecurityException("Invalid audience");
        }

        if (claims.getExpirationTime().before(new Date())) {
            throw new SecurityException("Token expired");
        }
        return claims;
    }

}
