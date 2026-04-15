package com.samsung.wallet.atw.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samsung.wallet.atw.demo.entity.BoardingPass;
import com.samsung.wallet.atw.demo.entity.Coupon;
import com.samsung.wallet.atw.demo.entity.IdCard;
import com.samsung.wallet.atw.demo.entity.Ticket;

/**
 * Builds Samsung Wallet card payloads for ticket, boarding pass, coupon, and id card.
 * Specs: Event Ticket, Boarding Pass, Coupon, Digital IDs on developer.samsung.com/wallet
 */
public final class CardPayloadBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Demo placeholder URLs for required image fields (replace with real URLs in production). */
    private static final String DEMO_MAIN_IMG = "https://developer.samsung.com/static/images/wallet-demo-main.png";
    private static final String DEMO_LOGO_LIGHT = "https://developer.samsung.com/static/images/wallet-demo-logo.png";
    private static final String DEMO_LOGO_DARK = "https://developer.samsung.com/static/images/wallet-demo-logo-dark.png";
    private static final String DEMO_APP_LINK_LOGO = "https://developer.samsung.com/static/images/wallet-demo-applink.png";
    private static final String DEFAULT_PROVIDER_NAME = "Demo Provider";
    private static final String DEFAULT_APP_LINK_NAME = "Demo App";
    private static final String DEFAULT_APP_LINK_DATA = "https://example.com";
    private static final String DEFAULT_NOTICE_DESC = "{\"count\":1,\"info\":[{\"title\":\"Notice\",\"content\":[\"Please present at the venue.\"]}]}";
    private static final String DEFAULT_CS_INFO = "{\"call\":\"0000-0000\",\"email\":\"cs@example.com\",\"webSite\":\"https://example.com/cs\"}";
    private static final String DEFAULT_CS_INFO_FULL = "{\"call\":\"0000-0000\",\"email\":\"samsungwallet@samsungwallet.com\",\"webSite\":\"https://www.samsungwallet.com/cs/\",\"instagram\":\"https://www.instagram.com/samsungwallet\",\"youtube\":\"https://www.youtube.com/@samsungwallet\",\"facebook\":\"https://www.facebook.com/samsungwallet\"}";
    private static final String DEMO_COVER_IMG = "https://developer.samsung.com/static/images/wallet-demo-cover.png";
    private static final String DEMO_BG_IMG = "https://developer.samsung.com/static/images/wallet-demo-bg.png";

    public static String toCardPayloadJson(Ticket ticket) throws JsonProcessingException {
        ObjectNode root = MAPPER.createObjectNode();
        ObjectNode card = MAPPER.createObjectNode();
        root.set("card", card);
        card.put("type", "ticket");
        card.put("subType", "sports");

        ArrayNode data = MAPPER.createArrayNode();
        ObjectNode item = MAPPER.createObjectNode();
        item.put("refId", ticket.getRefId());
        item.put("createdAt", ticket.getCreatedAt().toEpochMilli());
        item.put("updatedAt", ticket.getUpdatedAt().toEpochMilli());
        item.put("state", ticket.getStatus().name());
        item.put("language", "en");

        ObjectNode attrs = MAPPER.createObjectNode();

        // Required: title (String 32)
        attrs.put("title", truncate(ticket.getTitle() != null ? ticket.getTitle() : "Entrance Ticket", 32));

        // Required: mainImg (URL, max 512kB)
        attrs.put("mainImg", DEMO_MAIN_IMG);

        // Spec sample supports both flat logoImage and light/dark explicit keys.
        attrs.put("logoImage", DEMO_LOGO_DARK);
        attrs.put("logoImage.darkUrl", DEMO_LOGO_DARK);
        attrs.put("logoImage.lightUrl", DEMO_LOGO_LIGHT);

        // Required: providerName (String 32)
        attrs.put("providerName", truncate(ticket.getProviderName() != null ? ticket.getProviderName() : DEFAULT_PROVIDER_NAME, 32));

        // Required: issueDate (epoch ms)
        attrs.put("issueDate", ticket.getCreatedAt().toEpochMilli());

        // Required: reservationNumber (String 32)
        attrs.put("reservationNumber", truncate(
                ticket.getReservationNumber() != null ? ticket.getReservationNumber() : ticket.getRefId(),
                32));

        // Required: startDate (epoch ms)
        long startMs = ticket.getEventAt() != null ? ticket.getEventAt().toEpochMilli() : ticket.getCreatedAt().toEpochMilli();
        attrs.put("startDate", startMs);
        attrs.put("category", "Sports");
        attrs.put("eventId", "event-01");

        // Optional: endDate (epoch ms; if null, card expires 10h after startDate per spec)
        if (ticket.getEventAt() != null) {
            attrs.put("endDate", ticket.getEventAt().toEpochMilli() + 4 * 60 * 60 * 1000L); // +4h for demo
        }

        // Required: noticeDesc (String 5000, JSON format for structured notice)
        attrs.put("noticeDesc", DEFAULT_NOTICE_DESC);

        // Required: appLinkLogo, appLinkName, appLinkData
        attrs.put("appLinkLogo", DEMO_APP_LINK_LOGO);
        attrs.put("appLinkName", truncate(DEFAULT_APP_LINK_NAME, 32));
        attrs.put("appLinkData", truncate(DEFAULT_APP_LINK_DATA, 256));

        // Optional: entrance (gate), subtitle1 (venue)
        if (ticket.getGate() != null && !ticket.getGate().isBlank()) {
            attrs.put("entrance", truncate(ticket.getGate(), 64));
        }
        if (ticket.getVenue() != null && !ticket.getVenue().isBlank()) {
            attrs.put("subtitle1", truncate(ticket.getVenue(), 32));
        }
        // Samsung sample: groupingId, orderId, classification, holderName, grade, csInfo, bgColor, fontColor, blinkColor, barcode
        if (ticket.getGroupingId() != null && !ticket.getGroupingId().isBlank()) {
            attrs.put("groupingId", truncate(ticket.getGroupingId(), 32));
        }
        if (ticket.getOrderId() != null && !ticket.getOrderId().isBlank()) {
            attrs.put("orderId", truncate(ticket.getOrderId(), 32));
        }
        if (ticket.getClassification() != null && !ticket.getClassification().isBlank()) {
            attrs.put("classification", truncate(ticket.getClassification(), 16));
        }
        if (ticket.getHolderName() != null && !ticket.getHolderName().isBlank()) {
            attrs.put("holderName", truncate(ticket.getHolderName(), 64));
            attrs.put("user", truncate(ticket.getHolderName(), 64));
            attrs.put("certification", truncate(ticket.getHolderName(), 64));
        }
        if (ticket.getGrade() != null && !ticket.getGrade().isBlank()) {
            attrs.put("grade", truncate(ticket.getGrade(), 32));
        }
        attrs.put("seatClass", "Standard");
        attrs.put("seatNumber", "A-81");
        attrs.put("reactivatableYn", "N");
        attrs.put("preventCaptureYn", "N");
        attrs.put("noNetworkSupportYn", "N");
        attrs.put("person1", "{\"person\":[{\"category\":\"Adult\",\"count\":1}]}");
        attrs.put("locations", "[{\"name\":\"Lions Ballpark\",\"address\":\"129 Samsung-ro Yeongtong-gu Suwon-si\",\"lat\":37.255993,\"lng\":127.051112}]");
        attrs.put("groupInfo1", "Adult 1");
        attrs.put("groupInfo2", "Standard");
        attrs.put("groupInfo3", "Family");
        attrs.put("csInfo", DEFAULT_CS_INFO_FULL);
        attrs.put("privacyModeYn", "N");
        attrs.put("bgColor", "#E86D1F");
        attrs.put("fontColor", "light");
        attrs.put("blinkColor", "#E86D1F");
        putBarcode(attrs, ticket.getBarcodeValue() != null ? ticket.getBarcodeValue() : ticket.getRefId(), "QRCODE", "QRCODESERIAL", "QR_CODE");

        item.set("attributes", attrs);
        data.add(item);
        card.set("data", data);

        return MAPPER.writeValueAsString(root);
    }

    /**
     * Boarding pass (airlines) payload per Samsung spec.
     * https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/boardingpass.html
     */
    public static String toBoardingPassPayloadJson(BoardingPass bp) throws JsonProcessingException {
        ObjectNode root = MAPPER.createObjectNode();
        ObjectNode card = MAPPER.createObjectNode();
        root.set("card", card);
        card.put("type", "boardingpass");
        card.put("subType", "airlines");

        ArrayNode data = MAPPER.createArrayNode();
        ObjectNode item = MAPPER.createObjectNode();
        item.put("refId", bp.getRefId());
        item.put("createdAt", bp.getCreatedAt().toEpochMilli());
        item.put("updatedAt", bp.getUpdatedAt().toEpochMilli());
        item.put("language", "en");

        ObjectNode attrs = MAPPER.createObjectNode();
        attrs.put("title", truncate(bp.getTitle() != null ? bp.getTitle() : "BOARDING PASS", 32));
        ObjectNode providerLogo = MAPPER.createObjectNode();
        providerLogo.put("darkUrl", DEMO_LOGO_DARK);
        providerLogo.put("lightUrl", DEMO_LOGO_LIGHT);
        attrs.set("providerLogo", providerLogo);
        attrs.put("providerName", truncate(bp.getProviderName() != null ? bp.getProviderName() : DEFAULT_PROVIDER_NAME, 32));
        attrs.put("user", truncate(bp.getPassengerName() != null ? bp.getPassengerName() : "Passenger", 64));
        attrs.put("vehicleNumber", truncate(bp.getVehicleNumber() != null ? bp.getVehicleNumber() : "FLT001", 32));
        attrs.put("seatClass", truncate(bp.getSeatClass() != null ? bp.getSeatClass() : "Economy", 32));
        attrs.put("seatNumber", truncate(bp.getSeatNumber() != null ? bp.getSeatNumber() : "12A", 16));
        attrs.put("reservationNumber", truncate(bp.getReservationNumber() != null ? bp.getReservationNumber() : bp.getRefId(), 32));
        attrs.put("departName", truncate(bp.getDepartName() != null ? bp.getDepartName() : "Seoul/Incheon", 32));
        attrs.put("departCode", truncate(bp.getDepartCode() != null ? bp.getDepartCode() : "ICN", 8));
        attrs.put("departTerminal", truncate(bp.getDepartTerminal() != null ? bp.getDepartTerminal() : "2", 8));
        attrs.put("arriveName", truncate(bp.getArriveName() != null ? bp.getArriveName() : "San Francisco", 32));
        attrs.put("arriveCode", truncate(bp.getArriveCode() != null ? bp.getArriveCode() : "SFO", 8));
        if (bp.getGroupingId() != null && !bp.getGroupingId().isBlank()) {
            attrs.put("groupingId", truncate(bp.getGroupingId(), 32));
        }
        if (bp.getDepartGate() != null && !bp.getDepartGate().isBlank()) {
            attrs.put("departGate", truncate(bp.getDepartGate(), 8));
        }
        if (bp.getArriveTerminal() != null && !bp.getArriveTerminal().isBlank()) {
            attrs.put("arriveTerminal", truncate(bp.getArriveTerminal(), 8));
        }
        if (bp.getArriveGate() != null && !bp.getArriveGate().isBlank()) {
            attrs.put("arriveGate", truncate(bp.getArriveGate(), 8));
        }
        if (bp.getBaggageAllowance() != null && !bp.getBaggageAllowance().isBlank()) {
            attrs.put("baggageAllowance", truncate(bp.getBaggageAllowance(), 16));
        }
        if (bp.getBoardingSeqNo() != null && !bp.getBoardingSeqNo().isBlank()) {
            attrs.put("boardingSeqNo", truncate(bp.getBoardingSeqNo(), 32));
        }
        long departMs = bp.getDepartAt() != null ? bp.getDepartAt().toEpochMilli() : bp.getCreatedAt().toEpochMilli();
        long arriveMs = bp.getArriveAt() != null ? bp.getArriveAt().toEpochMilli() : departMs + 3600000L * 11;
        attrs.put("estimatedOrActualStartDate", departMs);
        attrs.put("estimatedOrActualStartDate.utcOffset", "UTC+09:00");
        attrs.put("estimatedOrActualEndDate", arriveMs);
        attrs.put("estimatedOrActualEndDate.utcOffset", "UTC-08:00");
        attrs.put("boardingTime", departMs);
        attrs.put("boardingTime.utcOffset", "UTC+09:00");
        attrs.put("bgColor", "#015AAA");
        attrs.put("fontColor", "light");
        attrs.put("appLinkLogo", DEMO_APP_LINK_LOGO);
        attrs.put("appLinkName", truncate(DEFAULT_APP_LINK_NAME, 32));
        attrs.put("appLinkData", truncate(DEFAULT_APP_LINK_DATA, 256));
        attrs.put("csInfo", DEFAULT_CS_INFO_FULL);
        putBarcode(attrs, bp.getBarcodeValue() != null ? bp.getBarcodeValue() : bp.getRefId(), "BARCODE", "QRCODESERIAL", "QR_CODE");

        item.set("attributes", attrs);
        data.add(item);
        card.set("data", data);
        return MAPPER.writeValueAsString(root);
    }

    /**
     * Coupon payload per Samsung spec.
     * https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/coupon.html
     */
    public static String toCouponPayloadJson(Coupon c) throws JsonProcessingException {
        ObjectNode root = MAPPER.createObjectNode();
        ObjectNode card = MAPPER.createObjectNode();
        root.set("card", card);
        card.put("type", "coupon");
        card.put("subType", "others");

        ArrayNode data = MAPPER.createArrayNode();
        ObjectNode item = MAPPER.createObjectNode();
        item.put("refId", c.getRefId());
        item.put("createdAt", c.getCreatedAt().toEpochMilli());
        item.put("updatedAt", c.getUpdatedAt().toEpochMilli());
        item.put("language", "en");

        ObjectNode attrs = MAPPER.createObjectNode();
        attrs.put("title", truncate(c.getTitle() != null ? c.getTitle() : "Free Coupon", 32));
        attrs.put("mainImg", DEMO_MAIN_IMG);
        attrs.put("expiry", c.getExpiryAt().toEpochMilli());
        attrs.put("issueDate", c.getCreatedAt().toEpochMilli());
        attrs.put("editableYn", "N");
        attrs.put("deletableYn", "Y");
        attrs.put("displayRedeemButtonYn", "Y");
        attrs.put("notificationYn", "Y");
        attrs.put("appLinkLogo", DEMO_APP_LINK_LOGO);
        attrs.put("appLinkName", truncate(DEFAULT_APP_LINK_NAME, 32));
        attrs.put("appLinkData", truncate(DEFAULT_APP_LINK_DATA, 256));
        attrs.put("preventCaptureYn", "N");
        if (c.getBrandName() != null && !c.getBrandName().isBlank()) {
            attrs.put("brandName", truncate(c.getBrandName(), 32));
        }
        putBarcode(attrs, c.getBarcodeValue() != null ? c.getBarcodeValue() : c.getRefId(), "BARCODE", "QRCODESERIAL", "QR_CODE");

        item.set("attributes", attrs);
        data.add(item);
        card.set("data", data);
        return MAPPER.writeValueAsString(root);
    }

    /**
     * Digital ID card payload per Samsung spec.
     * https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/digitalids.html
     */
    public static String toIdCardPayloadJson(IdCard id) throws JsonProcessingException {
        ObjectNode root = MAPPER.createObjectNode();
        ObjectNode card = MAPPER.createObjectNode();
        root.set("card", card);
        card.put("type", "idcard");
        card.put("subType", "employees");

        ArrayNode data = MAPPER.createArrayNode();
        ObjectNode item = MAPPER.createObjectNode();
        item.put("refId", id.getRefId());
        item.put("createdAt", id.getCreatedAt().toEpochMilli());
        item.put("updatedAt", id.getUpdatedAt().toEpochMilli());
        item.put("language", "en");

        ObjectNode attrs = MAPPER.createObjectNode();
        attrs.put("title", truncate(id.getTitle() != null ? id.getTitle() : "ID Card", 32));
        attrs.put("holderName", truncate(id.getHolderName(), 64));
        attrs.put("identifier", truncate(id.getIdentifier(), 64));
        attrs.put("issueDate", id.getCreatedAt().toEpochMilli());
        attrs.put("providerName", truncate(id.getProviderName() != null ? id.getProviderName() : DEFAULT_PROVIDER_NAME, 32));
        attrs.put("csInfo", DEFAULT_CS_INFO);
        attrs.put("appLinkLogo", DEMO_APP_LINK_LOGO);
        attrs.put("appLinkName", truncate(DEFAULT_APP_LINK_NAME, 32));
        attrs.put("appLinkData", truncate(DEFAULT_APP_LINK_DATA, 256));
        if (id.getOrganization() != null && !id.getOrganization().isBlank()) {
            attrs.put("organization", truncate(id.getOrganization(), 64));
        }
        if (id.getPosition() != null && !id.getPosition().isBlank()) {
            attrs.put("position", truncate(id.getPosition(), 64));
        }
        if (id.getExpiryAt() != null) {
            attrs.put("expiry", id.getExpiryAt().toEpochMilli());
        }
        if (id.getSecondHolderName() != null && !id.getSecondHolderName().isBlank()) {
            attrs.put("secondHolderName", truncate(id.getSecondHolderName(), 64));
        }
        if (id.getIdNumber() != null && !id.getIdNumber().isBlank()) {
            attrs.put("idNumber", truncate(id.getIdNumber(), 64));
        }
        attrs.put("extraInfo", "{\"count\":1,\"info\":[{\"title\":\"shortCode\",\"content\":[\"404457\"]}]}");
        attrs.put("noticeDesc", "{\"count\":2,\"info\":[{\"title\":\"NOTICE1\",\"content\":[\"DESCRIPTION1\",\"DESCRIPTION2\"]},{\"title\":\"NOTICE2\",\"content\":[\"DESCRIPTION1\",\"DESCRIPTION2\"]}]}");
        attrs.put("coverImage", DEMO_COVER_IMG);
        attrs.put("bgImage", DEMO_BG_IMG);
        attrs.put("fontColor", "dark");
        putBarcode(attrs, id.getIdentifier(), "QRCODE", "QRCODE", "QR_CODE");

        item.set("attributes", attrs);
        data.add(item);
        card.set("data", data);
        return MAPPER.writeValueAsString(root);
    }

    /** Sets barcode.value, barcode.serialType, barcode.ptFormat, barcode.ptSubFormat on attrs (Samsung sample). */
    private static void putBarcode(ObjectNode attrs, String value, String serialType, String ptFormat, String ptSubFormat) {
        if (value != null && !value.isBlank()) {
            attrs.put("barcode.value", truncate(value, 4096));
        }
        attrs.put("barcode.serialType", serialType);
        attrs.put("barcode.ptFormat", ptFormat);
        attrs.put("barcode.ptSubFormat", ptSubFormat);
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }
}
