package com.samsung.wallet.atw.demo.config;

import com.samsung.wallet.atw.crypto.AuthTokenValidator;
import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Validates Authorization Bearer token on partner inbound endpoints (Get Card Data, Send Card State).
 */
@Component
@Order(1)
public class AuthValidationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthValidationFilter.class);
    private final AuthTokenValidator validator;

    public AuthValidationFilter(WalletCryptoConfig config) {
        this.validator = new AuthTokenValidator(config);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (!path.startsWith("/partner/") && !path.startsWith("/cards/")) {
            chain.doFilter(request, response);
            return;
        }

        long startMs = System.currentTimeMillis();
        String requestId = req.getHeader("x-request-id");
        String refId = req.getHeader("x-ref-id");
        String method = req.getMethod();
        String auth = req.getHeader("Authorization");
        String maskedAuth = maskToken(auth);

        log.info("Inbound request id={} method={} path={} refId={} auth={}",
                valueOrDash(requestId), method, path, valueOrDash(refId), maskedAuth);

        // Samsung spec: token binds to path only, excluding scheme/host/query
        if (!validator.validate(auth, method, path)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"Invalid or missing Authorization\"}");
            long elapsed = System.currentTimeMillis() - startMs;
            log.error("Inbound auth failed id={} method={} path={} status={} elapsedMs={}",
                    valueOrDash(requestId), method, path, HttpServletResponse.SC_UNAUTHORIZED, elapsed);
            return;
        }
        try {
            chain.doFilter(request, response);
            long elapsed = System.currentTimeMillis() - startMs;
            log.info("Inbound response id={} method={} path={} status={} elapsedMs={}",
                    valueOrDash(requestId), method, path, res.getStatus(), elapsed);
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startMs;
            log.error("Inbound processing error id={} method={} path={} status={} elapsedMs={} message={}",
                    valueOrDash(requestId), method, path, res.getStatus(), elapsed, e.getMessage(), e);
            throw e;
        }
    }

    private static String maskToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return "-";
        }
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7).trim() : authHeader.trim();
        if (token.isEmpty()) {
            return "Bearer(empty)";
        }
        int visible = Math.min(12, token.length());
        return "Bearer(" + token.substring(0, visible) + "...masked)";
    }

    private static String valueOrDash(String value) {
        return (value == null || value.isBlank()) ? "-" : value;
    }
}
