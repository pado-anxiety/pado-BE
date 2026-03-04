package com.pado.log;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MDCLoggingFilter implements Filter {

    @Value("${logging.execution-time.enabled:false}")
    private boolean executionTimeEnabled;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestId = request.getHeader("X-RequestID");
        if (StringUtils.isBlank(requestId)) {
            requestId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put("request_id", requestId);
        MDC.put("endpoint", request.getRequestURI());
        MDC.put("httpMethod", request.getMethod());
        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (executionTimeEnabled) {
                long end = System.currentTimeMillis();
                MDC.put("executionTime", String.valueOf(end - start));
                log.info("request completed");
            }
            MDC.clear();
        }
    }
}
