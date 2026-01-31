package com.pado.auth.infrastructure.oauth.kakao;

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
public class KakaoIdentityTokenVerifier {

    private static final String URL = "https://kauth.kakao.com/.well-known/jwks.json";

    @Value("${oauth2.client.registration.kakao.client-id}")
    private String clientId;

    public KakaoClaims verify(String identityToken) {
        try {
            JWKSet jwkSet = JWKSet.load(new URL(URL));
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
            RSAKey rsaKey = (RSAKey) jwk;
            RSASSAVerifier verifier = new RSASSAVerifier(rsaKey);
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("kakao identity token 검증에 실패했습니다.");
            }

            JWTClaimsSet claims = getJwtClaimsSet(signedJWT);
            return new KakaoClaims(claims.getSubject(), claims.getStringClaim("email"), claims.getStringClaim("nickname"));

        } catch (IOException | ParseException | JOSEException e) {
            throw new RuntimeException("kakao identity token 검증 중 오류가 발생했습니다.", e);
        }
    }

    private @NotNull JWTClaimsSet getJwtClaimsSet(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        if (!"https://kauth.kakao.com".equals(claims.getIssuer())) {
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
