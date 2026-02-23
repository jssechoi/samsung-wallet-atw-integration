package com.samsung.wallet.atw.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Loads partner private key and Samsung/public certificates from files or classpath.
 * Framework-agnostic; use from any Java environment.
 */
public final class KeyStoreLoader {

    private KeyStoreLoader() {}

    /**
     * Load a PEM-encoded private key (PKCS#8). Supports "-----BEGIN PRIVATE KEY-----" format.
     */
    public static PrivateKey loadPrivateKeyFromPem(Path path) throws IOException, GeneralSecurityException {
        String pem = Files.readString(path);
        return loadPrivateKeyFromPemString(pem);
    }

    public static PrivateKey loadPrivateKeyFromPemString(String pem) throws GeneralSecurityException {
        String base64 = pem
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Load X.509 certificate from PEM file.
     */
    public static X509Certificate loadCertificateFromPem(Path path) throws IOException, CertificateException {
        try (InputStream is = Files.newInputStream(path)) {
            return loadCertificateFromStream(is);
        }
    }

    public static X509Certificate loadCertificateFromStream(InputStream is) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(is);
        if (!(cert instanceof X509Certificate)) {
            throw new IllegalArgumentException("Expected X.509 certificate");
        }
        return (X509Certificate) cert;
    }

    /**
     * Extract public key from X.509 certificate.
     */
    public static PublicKey getPublicKey(X509Certificate cert) {
        return cert.getPublicKey();
    }
}
