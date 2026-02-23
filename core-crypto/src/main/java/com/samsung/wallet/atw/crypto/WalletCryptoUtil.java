package com.samsung.wallet.atw.crypto;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import net.minidev.json.JSONObject;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates Samsung Wallet tokens: cdata (JWS-wrapped JWE) and REST API Authorization token.
 * See Samsung docs: Card Data Token (cdata), REST API Authorization Token.
 */
public final class WalletCryptoUtil {

    /** Default TTL for cdata (seconds). Samsung recommends generating cdata on user action and short validity. */
    public static final int CDATA_TTL_SECONDS = 30;

    private final WalletCryptoConfig config;

    public WalletCryptoUtil(WalletCryptoConfig config) {
        this.config = config;
    }

    /**
     * Build cdata token: JWS( JWE(cardPayload) ). Outer JWS signed with partner key;
     * inner JWE encrypted with Samsung public key. TTL default 30 seconds.
     */
    public String buildCData(String cardPayloadJson, int ttlSeconds) throws JOSEException {
        long now = System.currentTimeMillis();
        long exp = now + ttlSeconds * 1000L;

        // 1) Inner JWE: encrypt card payload with Samsung's public key
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.RSA1_5, EncryptionMethod.A128GCM)
                .build();
        Payload jwePayload = new Payload(cardPayloadJson);
        JWEObject jwe = new JWEObject(jweHeader, jwePayload);
        jwe.encrypt(new RSAEncrypter((RSAPublicKey) config.getSamsungPublicKey()));
        String jweCompact = jwe.serialize();

        // 2) Outer JWS: sign the JWE string with partner's private key (Samsung spec: JWS-wrapped JWE)
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .contentType("CARD")
                .customParam("ver", "3")
                .customParam("certificateId", config.getCertificateId())
                .customParam("partnerId", config.getPartnerId())
                .customParam("utc", now)
                .build();
        JWSObject jws = new JWSObject(jwsHeader, new Payload(jweCompact));
        jws.sign(new RSASSASigner(config.getPartnerPrivateKey()));
        return jws.serialize();
    }

    /**
     * Build cdata with default 30-second TTL.
     */
    public String buildCData(String cardPayloadJson) throws JOSEException {
        return buildCData(cardPayloadJson, CDATA_TTL_SECONDS);
    }

    /**
     * Build REST API Authorization Token (JWT/JWS) for calling Samsung APIs (e.g. Update Notification).
     * Payload must bind to the actual request: API.method and API.path (path only, no scheme/host/query).
     */
    public String buildAuthToken(String method, String path) throws JOSEException, ParseException {
        long now = System.currentTimeMillis();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("API", Map.of("method", method, "path", path))
                .build();

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "RS256");
        header.put("cty", "AUTH");
        header.put("ver", "3");
        header.put("certificateId", config.getCertificateId());
        header.put("partnerId", config.getPartnerId());
        header.put("utc", now);

        JWSHeader jwsHeader = JWSHeader.parse(header);
        JSONObject payloadJson = new JSONObject(claims.toJSONObject());
        JWSObject jws = new JWSObject(jwsHeader, new Payload(payloadJson));
        jws.sign(new RSASSASigner(config.getPartnerPrivateKey()));
        return jws.serialize();
    }

    /**
     * For Data Fetch link: pdata is the refId (or an opaque token that your server maps to refId).
     * This method returns the refId as-is; the client builds the ATW URL with pdata=refId.
     * If your flow uses an encrypted/signed pdata token, extend this to wrap refId in a short-lived JWT.
     */
    public String buildPDataRefId(String refId) {
        return refId;
    }
}
