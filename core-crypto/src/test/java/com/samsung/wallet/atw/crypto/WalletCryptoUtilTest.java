package com.samsung.wallet.atw.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for cdata and auth token generation. Uses a temporary key pair in place of
 * partner/Samsung keys; in production replace with real certificates.
 */
class WalletCryptoUtilTest {

    private WalletCryptoConfig config;
    private WalletCryptoUtil util;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair partnerKey = kpg.generateKeyPair();
        KeyPair samsungKey = kpg.generateKeyPair();
        config = new WalletCryptoConfig(
                "partner-001",
                "card-001",
                "cert-001",
                partnerKey.getPrivate(),
                samsungKey.getPublic()
        );
        util = new WalletCryptoUtil(config);
    }

    @Test
    void buildCData_returnsNonEmptyToken() throws Exception {
        String cardJson = "{\"type\":\"ticket\",\"data\":[{\"refId\":\"ref-1\"}]}";
        String cdata = util.buildCData(cardJson, 30);
        assertNotNull(cdata);
        assertTrue(cdata.split("\\.").length >= 3);
    }

    @Test
    void buildCData_defaultTtl_returnsNonEmptyToken() throws Exception {
        String cardJson = "{\"type\":\"ticket\"}";
        String cdata = util.buildCData(cardJson);
        assertNotNull(cdata);
    }

    @Test
    void buildAuthToken_returnsNonEmptyToken() throws Exception {
        String token = util.buildAuthToken("POST", "/wltex/cards/card-001/updates");
        assertNotNull(token);
        assertTrue(token.split("\\.").length >= 3);
    }

    @Test
    void buildPDataRefId_returnsRefId() {
        String pdata = util.buildPDataRefId("ref-ticket-001");
        assertNotNull(pdata);
        assertTrue(pdata.equals("ref-ticket-001"));
    }
}
