package com.pado.auth.infrastructure.jwt;

import com.pado.auth.controller.dto.TokenResponse;
import com.pado.auth.infrastructure.AuthUser;
import com.pado.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

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

    public String createAccessToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("tz", user.getTimezone())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponse reissueTokens(User user, String refreshToken) {
        validateRefreshToken(refreshToken, user);
        return new TokenResponse(createAccessToken(user), createRefreshToken(user.getId()));
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
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

        Long userId = Long.valueOf(claims.getSubject());
        String timezone = claims.get("tz", String.class);

        AuthUser principal = new AuthUser(userId, timezone);

        return new UsernamePasswordAuthenticationToken(principal, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(
                Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
                        .getSubject());
    }

}