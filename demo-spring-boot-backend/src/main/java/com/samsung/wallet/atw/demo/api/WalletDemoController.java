package com.samsung.wallet.atw.demo.api;

import com.samsung.wallet.atw.demo.entity.BoardingPass;
import com.samsung.wallet.atw.demo.entity.Coupon;
import com.samsung.wallet.atw.demo.entity.IdCard;
import com.samsung.wallet.atw.demo.entity.Ticket;
import com.samsung.wallet.atw.demo.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Demo API for frontend: transit-data (cdata), fetch-data (pdata), update-notification.
 * Serves the Thymeleaf page with tickets, boarding passes, coupons, and id cards.
 */
@Controller
public class WalletDemoController {

    private final TicketService ticketService;
    private final BoardingPassService boardingPassService;
    private final CouponService couponService;
    private final IdCardService idCardService;
    private final CDataService cDataService;
    private final SamsungWalletNotificationClient notificationClient;

    @Value("${samsung.wallet.partner-id:demo-partner}")
    private String partnerId;

    @Value("${samsung.wallet.card-id:demo-card}")
    private String cardId;

    @Value("${samsung.wallet.certificate-id:demo-cert}")
    private String certificateId;

    @Value("${samsung.wallet.card-id-ticket:3iulq9hr93hg0}")
    private String ticketCardId;
    @Value("${samsung.wallet.card-id-boardingpass:3iumevlevet00}")
    private String boardingPassCardId;
    @Value("${samsung.wallet.card-id-coupon:3iumf1neuhjg0}")
    private String couponCardId;
    @Value("${samsung.wallet.card-id-idcard:3iumf2sua70g0}")
    private String idCardCardId;

    public WalletDemoController(TicketService ticketService,
                               BoardingPassService boardingPassService,
                               CouponService couponService,
                               IdCardService idCardService,
                               CDataService cDataService,
                               SamsungWalletNotificationClient notificationClient) {
        this.ticketService = ticketService;
        this.boardingPassService = boardingPassService;
        this.couponService = couponService;
        this.idCardService = idCardService;
        this.cDataService = cDataService;
        this.notificationClient = notificationClient;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        model.addAttribute("boardingPasses", boardingPassService.findAll());
        model.addAttribute("coupons", couponService.findAll());
        model.addAttribute("idCards", idCardService.findAll());
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("cardId", cardId);
        model.addAttribute("certificateId", certificateId);
        model.addAttribute("ticketCardId", ticketCardId);
        model.addAttribute("boardingPassCardId", boardingPassCardId);
        model.addAttribute("couponCardId", couponCardId);
        model.addAttribute("idCardCardId", idCardCardId);
        return "index";
    }

    /**
     * Data Transmit: return cdata for the given refId and card type.
     * cardType: ticket (default), boardingpass, coupon, idcard.
     */
    @GetMapping("/api/v1/wallet/transit-data")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getTransitData(
            @RequestParam("refId") String refId,
            @RequestParam(value = "type", defaultValue = "ticket") String type) {
        try {
            String cdata = cDataService.buildCData(refId, type);
            return ResponseEntity.ok(Map.of("cdata", cdata, "refId", refId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Data Fetch: return pdata (refId) for the given refId. Frontend builds ATW link with pdata=...
     * type is optional; used only to verify the card exists.
     */
    @GetMapping("/api/v1/wallet/fetch-data")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getFetchData(
            @RequestParam("refId") String refId,
            @RequestParam(value = "type", defaultValue = "ticket") String type) {
        if (!cardExists(refId, type)) {
            return ResponseEntity.notFound().build();
        }
        String pdata = cDataService.buildPDataRefId(refId);
        return ResponseEntity.ok(Map.of("pdata", pdata, "refId", refId));
    }

    private boolean cardExists(String refId, String cardType) {
        return switch (cardType == null || cardType.isBlank() ? "ticket" : cardType.toLowerCase()) {
            case "boardingpass" -> boardingPassService.findByRefId(refId) != null;
            case "coupon" -> couponService.findByRefId(refId) != null;
            case "idcard" -> idCardService.findByRefId(refId) != null;
            default -> ticketService.findByRefId(refId) != null;
        };
    }

    /**
     * Trigger Update Notification: partner calls Samsung so wallet refreshes the card.
     * cardType: ticket (default), boardingpass, coupon, idcard — determines which cardId to use.
     */
    @PostMapping("/api/v1/wallet/update-notification")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateNotification(@RequestBody Map<String, String> body) {
        String refId = body.get("refId");
        String state = body.getOrDefault("state", "UPDATED");
        String cardType = body.getOrDefault("cardType", "ticket");
        if (refId == null || refId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "refId required"));
        }
        String targetCardId = cardIdForType(cardType);
        try {
            int status = notificationClient.sendUpdateNotification(refId, state, targetCardId, cardType);
            return ResponseEntity.ok(Map.of("refId", refId, "status", status));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    private String cardIdForType(String cardType) {
        if (cardType == null) return ticketCardId;
        return switch (cardType.toLowerCase()) {
            case "boardingpass" -> boardingPassCardId;
            case "coupon" -> couponCardId;
            case "idcard" -> idCardCardId;
            default -> ticketCardId;
        };
    }
}
