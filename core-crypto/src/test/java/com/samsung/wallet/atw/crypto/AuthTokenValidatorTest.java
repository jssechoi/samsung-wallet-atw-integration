package com.samsung.wallet.atw.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenValidatorTest {

    private WalletCryptoConfig config;
    private WalletCryptoUtil util;
    private AuthTokenValidator validator;

    private KeyPair partnerKey;
    private KeyPair samsungKey;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        partnerKey = kpg.generateKeyPair();
        samsungKey = kpg.generateKeyPair();
        config = new WalletCryptoConfig(
                "partner-001", "card-001", "cert-001",
                partnerKey.getPrivate(),
                samsungKey.getPublic()
        );
        util = new WalletCryptoUtil(config);
        validator = new AuthTokenValidator(config);
    }

    @Test
    void extractBearerToken_nullReturnsNull() {
        assertNull(AuthTokenValidator.extractBearerToken(null));
    }

    @Test
    void extractBearerToken_noBearerReturnsNull() {
        assertNull(AuthTokenValidator.extractBearerToken("Basic xyz"));
    }

    @Test
    void extractBearerToken_returnsTokenWithoutBearer() {
        assertEquals("abc123", AuthTokenValidator.extractBearerToken("Bearer abc123"));
    }

    @Test
    void validate_invalidTokenReturnsFalse() {
        assertFalse(validator.validate("Bearer invalid.jwt.here", "GET", "/partner/v1/tickets/ref-1"));
    }

    @Test
    void validate_emptyReturnsFalse() {
        assertFalse(validator.validate("", "GET", "/partner/v1/tickets/ref-1"));
    }

    @Test
    void validate_tokenSignedBySamsungPublicKey_returnsTrue() throws Exception {
        // Simulate Samsung: build auth token signed with Samsung's private key
        WalletCryptoConfig samsungSignerConfig = new WalletCryptoConfig(
                "partner-001", "card-001", "cert-001",
                samsungKey.getPrivate(),
                partnerKey.getPublic()
        );
        WalletCryptoUtil samsungUtil = new WalletCryptoUtil(samsungSignerConfig);
        String path = "/partner/v1/tickets/ref-1";
        String token = samsungUtil.buildAuthToken("GET", path);
        assertTrue(validator.validate("Bearer " + token, "GET", path));
    }
}
