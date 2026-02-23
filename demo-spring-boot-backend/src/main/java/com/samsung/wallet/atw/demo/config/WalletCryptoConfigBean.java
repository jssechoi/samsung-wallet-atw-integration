package com.samsung.wallet.atw.demo.config;

import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import com.samsung.wallet.atw.crypto.KeyStoreLoader;
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
        if (partnerPrivateKeyPath != null && !partnerPrivateKeyPath.isBlank()
                && Files.exists(Path.of(partnerPrivateKeyPath))
                && samsungPublicCertPath != null && !samsungPublicCertPath.isBlank()
                && Files.exists(Path.of(samsungPublicCertPath))) {
            partnerKey = KeyStoreLoader.loadPrivateKeyFromPem(Path.of(partnerPrivateKeyPath));
            X509Certificate samsungCert = KeyStoreLoader.loadCertificateFromPem(Path.of(samsungPublicCertPath));
            samsungPublic = KeyStoreLoader.getPublicKey(samsungCert);
        } else {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair partnerKp = kpg.generateKeyPair();
            KeyPair samsungKp = kpg.generateKeyPair();
            partnerKey = partnerKp.getPrivate();
            samsungPublic = samsungKp.getPublic();
        }
        return new WalletCryptoConfig(partnerId, cardId, certificateId, partnerKey, samsungPublic);
    }
}
