package com.nyangtodac.auth.infrastructure.jwt;

import com.nyangtodac.auth.controller.dto.TokenResponse;
import com.nyangtodac.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration.access-token}") long accessTokenExpiration,
        @Value("${jwt.expiration.refresh-token}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, accessTokenExpiration);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, refreshTokenExpiration);
    }

    private String createToken(Long userId, long validityInMilliseconds) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public TokenResponse reissueTokens(User user, String refreshToken) {
        validateRefreshToken(refreshToken, user);
        return new TokenResponse(createAccessToken(user.getId()), createRefreshToken(user.getId()));
    }

    public void validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new AccessTokenInvalidException(e);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException(e);
        }
    }

    private void validateRefreshToken(String refreshToken, User user) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new RefreshTokenInvalidException(e);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException(e);
        }

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RefreshTokenMismatchException();
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            .getBody();
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(claims.getSubject())
            .password("")
            .authorities("ROLE_USER")
            .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

}