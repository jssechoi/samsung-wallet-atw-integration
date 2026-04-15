package com.samsung.wallet.atw.demo.config;

import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import com.samsung.wallet.atw.crypto.KeyStoreLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * Loads partner/Samsung keys and builds WalletCryptoConfig. If paths are not set, uses in-memory keys for demo.
 */
@Configuration
public class WalletCryptoConfigBean {
    private static final Logger log = LoggerFactory.getLogger(WalletCryptoConfigBean.class);

    @Value("${samsung.wallet.partner-id:demo-partner}")
    private String partnerId;

    @Value("${samsung.wallet.card-id:demo-card}")
    private String cardId;

    @Value("${samsung.wallet.certificate-id:demo-cert}")
    private String certificateId;

    @Value("${samsung.wallet.partner-private-key-path:}")
    private String partnerPrivateKeyPath;

    @Value("${samsung.wallet.samsung-public-cert-path:}")
    private String samsungPublicCertPath;

    @Bean
    public WalletCryptoConfig walletCryptoConfig() throws Exception {
        PrivateKey partnerKey;
        PublicKey samsungPublic;
        boolean hasPartnerKeyPath = partnerPrivateKeyPath != null && !partnerPrivateKeyPath.isBlank();
        boolean hasSamsungCertPath = samsungPublicCertPath != null && !samsungPublicCertPath.isBlank();
        boolean partnerKeyExists = hasPartnerKeyPath && Files.exists(Path.of(partnerPrivateKeyPath));
        boolean samsungCertExists = hasSamsungCertPath && Files.exists(Path.of(samsungPublicCertPath));

        if (partnerPrivateKeyPath != null && !partnerPrivateKeyPath.isBlank()
                && Files.exists(Path.of(partnerPrivateKeyPath))
                && samsungPublicCertPath != null && !samsungPublicCertPath.isBlank()
                && Files.exists(Path.of(samsungPublicCertPath))) {
            log.info("WalletCryptoConfig: loading configured key/cert partnerKeyPath={} samsungCertPath={}",
                    partnerPrivateKeyPath, samsungPublicCertPath);
            partnerKey = KeyStoreLoader.loadPrivateKeyFromPem(Path.of(partnerPrivateKeyPath));
            X509Certificate samsungCert = KeyStoreLoader.loadCertificateFromPem(Path.of(samsungPublicCertPath));
            samsungPublic = KeyStoreLoader.getPublicKey(samsungCert);
        } else {
            log.warn(
                    "WalletCryptoConfig: configured key/cert missing, using in-memory fallback keys. hasPartnerPath={} partnerExists={} hasSamsungPath={} samsungExists={} partnerKeyPath={} samsungCertPath={}",
                    hasPartnerKeyPath, partnerKeyExists, hasSamsungCertPath, samsungCertExists,
                    valueOrDash(partnerPrivateKeyPath), valueOrDash(samsungPublicCertPath));
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair partnerKp = kpg.generateKeyPair();
            KeyPair samsungKp = kpg.generateKeyPair();
            partnerKey = partnerKp.getPrivate();
            samsungPublic = samsungKp.getPublic();
        }
        log.info("WalletCryptoConfig: initialized partnerId={} certificateId={}", partnerId, certificateId);
        return new WalletCryptoConfig(partnerId, cardId, certificateId, partnerKey, samsungPublic);
    }

    private static String valueOrDash(String value) {
        return (value == null || value.isBlank()) ? "-" : value;
    }
}
