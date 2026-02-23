package com.samsung.wallet.atw.demo.config;

import com.samsung.wallet.atw.crypto.AuthTokenValidator;
import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Validates Authorization Bearer token on partner inbound endpoints (Get Card Data, Send Card State).
 */
@Component
@Order(1)
public class AuthValidationFilter implements Filter {

    private final WalletCryptoConfig config;
    private final AuthTokenValidator validator;

    public AuthValidationFilter(WalletCryptoConfig config) {
        this.config = config;
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

        String auth = req.getHeader("Authorization");
        String method = req.getMethod();
        // Samsung spec: token binds to path only, excluding scheme/host/query
        if (!validator.validate(auth, method, path)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"Invalid or missing Authorization\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
