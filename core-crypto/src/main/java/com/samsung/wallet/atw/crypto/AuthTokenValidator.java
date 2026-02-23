package com.samsung.wallet.atw.crypto;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Validates the Authorization Bearer token sent by Samsung when calling partner endpoints
 * (Get Card Data, Send Card State). Verifies signature with Samsung's public key and
 * optionally checks request binding (method/path). See REST API Authorization Token doc.
 */
public final class AuthTokenValidator {

    private final WalletCryptoConfig config;

    public AuthTokenValidator(WalletCryptoConfig config) {
        this.config = config;
    }

    /**
     * Parse "Bearer &lt;token&gt;" and return the JWT string, or null if missing/invalid format.
     */
    public static String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authorizationHeader.substring(7).trim();
        return token.isEmpty() ? null : token;
    }

    /**
     * Verify the token signature with Samsung's public key and optionally validate that
     * the bound API.method and API.path match the incoming request.
     *
     * @param bearerToken   value of Authorization header (with or without "Bearer " prefix)
     * @param requestMethod HTTP method of the request (e.g. GET, POST)
     * @param requestPath   path only, no scheme/host/query (e.g. /partner/v1/tickets/ref-123)
     * @return true if signature is valid and (if present) method/path match
     */
    public boolean validate(String bearerToken, String requestMethod, String requestPath) {
        String token = bearerToken != null && bearerToken.startsWith("Bearer ")
                ? bearerToken.substring(7).trim()
                : bearerToken;
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            SignedJWT signedJwt = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) config.getSamsungPublicKey());
            if (!signedJwt.verify(verifier)) {
                return false;
            }
            JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
            @SuppressWarnings("unchecked")
            Map<String, String> api = (Map<String, String>) claims.getClaim("API");
            if (api != null) {
                String method = api.get("method");
                String path = api.get("path");
                if (method != null && !method.equalsIgnoreCase(requestMethod)) {
                    return false;
                }
                if (path != null && !path.equals(requestPath)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
