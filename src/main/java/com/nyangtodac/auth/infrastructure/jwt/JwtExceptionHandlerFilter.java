package com.nyangtodac.auth.infrastructure.jwt;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException e) {
            returnError(HttpStatus.UNAUTHORIZED, response, e);
        } catch (AccessTokenInvalidException e) {
            /**
             * 보안 위험
             */
            returnError(HttpStatus.FORBIDDEN, response, );
            log.error("Invalid AccessToken detected for request [{} {}] from IP [{}]",
                    request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), e);
        } catch (Exception e) {
            returnError(HttpStatus.INTERNAL_SERVER_ERROR, response, "Authentication failed");
            log.error("Unexpected authentication error for request [{} {}] from IP [{}]",
                    request.getMethod(), request.getRequestURI(), request.getRemoteAddr(), e);
        }
    }

    private void returnError(HttpStatus httpStatus, HttpServletResponse response, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json; charset=UTF-8");

    }
}