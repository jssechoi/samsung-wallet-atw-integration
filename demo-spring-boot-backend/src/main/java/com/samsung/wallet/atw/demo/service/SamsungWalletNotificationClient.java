package com.samsung.wallet.atw.demo.service;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import com.samsung.wallet.atw.crypto.WalletCryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Calls Samsung Wallet Update Notification API.
 * See <a href="https://developer.samsung.com/wallet/addtosamsungwallet/apiguidelines.html#Updating-Wallet-Card-Specs">Update Notification</a>:
 * POST {cc2}/wltex/cards/{cardId}/updates with Authorization Bearer, x-smcs-partner-id, x-request-id and card payload.
 */
@Service
public class SamsungWalletNotificationClient {

    private final WalletCryptoConfig config;
    private final WalletCryptoUtil util;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${samsung.wallet.api-base-url:https://tsapi-card.walletsvc.samsung.com}")
    private String apiBaseUrl;

    @Value("${samsung.wallet.cc2:us}")
    private String cc2;

    public SamsungWalletNotificationClient(WalletCryptoConfig config) {
        this.config = config;
        this.util = new WalletCryptoUtil(config);
    }

    /**
     * Notify Samsung that a card (refId) has been updated so the wallet can refresh.
     * @param cardId wallet card ID for the path (e.g. per-type card ID from partner portal)
     * @param cardType wallet card type for the payload: ticket, boardingpass, coupon, idcard
     */
    public int sendUpdateNotification(String refId, String state, String cardId, String cardType) throws JOSEException, ParseException {
        String path = "/" + cc2 + "/wltex/cards/" + cardId + "/updates";
        String authToken = util.buildAuthToken("POST", path);

        String url = apiBaseUrl + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("x-smcs-partner-id", config.getPartnerId());
        headers.set("x-request-id", java.util.UUID.randomUUID().toString());

        String type = (cardType != null && !cardType.isBlank()) ? cardType : "ticket";
        Map<String, Object> body = Map.of(
                "card", Map.of(
                        "type", type,
                        "data", java.util.List.of(
                                Map.of("refId", refId, "state", state)
                        )
                )
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        return response.getStatusCode().value();
    }
}
