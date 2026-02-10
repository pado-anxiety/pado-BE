package com.pado.auth.infrastructure;

import jakarta.annotation.Nonnull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && (AuthUser.class.isAssignableFrom(parameter.getParameterType()));
    }

    @Override
    public Object resolveArgument(
            @Nonnull MethodParameter parameter,
            @Nonnull ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            @Nonnull WebDataBinderFactory binderFactory
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new UnauthorizedException();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthUser authUser)) {
            throw new UnauthorizedException();
        }
        return authUser;
    }
}
