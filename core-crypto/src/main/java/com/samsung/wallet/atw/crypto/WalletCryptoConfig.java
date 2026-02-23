package com.samsung.wallet.atw.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Configuration for Samsung Wallet crypto operations. All IDs and keys are provided externally
 * (e.g. from Partner Portal and certificate files).
 */
public class WalletCryptoConfig {

    private final String partnerId;
    private final String cardId;
    private final String certificateId;
    private final PrivateKey partnerPrivateKey;
    private final PublicKey samsungPublicKey;  // for JWE encryption and/or auth token verification

    public WalletCryptoConfig(String partnerId, String cardId, String certificateId,
                             PrivateKey partnerPrivateKey, PublicKey samsungPublicKey) {
        this.partnerId = partnerId;
        this.cardId = cardId;
        this.certificateId = certificateId;
        this.partnerPrivateKey = partnerPrivateKey;
        this.samsungPublicKey = samsungPublicKey;
    }

    public String getPartnerId() { return partnerId; }
    public String getCardId() { return cardId; }
    public String getCertificateId() { return certificateId; }
    public PrivateKey getPartnerPrivateKey() { return partnerPrivateKey; }
    public PublicKey getSamsungPublicKey() { return samsungPublicKey; }
}
