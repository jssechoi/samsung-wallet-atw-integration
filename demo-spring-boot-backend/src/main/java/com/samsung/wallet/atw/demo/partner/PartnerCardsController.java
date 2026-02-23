package com.samsung.wallet.atw.demo.partner;

import com.samsung.wallet.atw.demo.entity.Ticket;
import com.samsung.wallet.atw.demo.service.CDataService;
import com.samsung.wallet.atw.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Samsung API format: GET/POST /cards/{cardId}/{refId}.
 * Resolves cardType from cardId and delegates to same logic as PartnerWebhookController.
 * Register these full URLs in Samsung Wallet Partner Portal per card.
 */
@RestController
@RequestMapping("/cards")
public class PartnerCardsController {

    private final TicketService ticketService;
    private final CDataService cDataService;

    @Value("${samsung.wallet.card-id-ticket:3iulq9hr93hg0}")
    private String ticketCardId;
    @Value("${samsung.wallet.card-id-boardingpass:3iumevlevet00}")
    private String boardingPassCardId;
    @Value("${samsung.wallet.card-id-coupon:3iumf1neuhjg0}")
    private String couponCardId;
    @Value("${samsung.wallet.card-id-idcard:3iumf2sua70g0}")
    private String idCardCardId;

    public PartnerCardsController(TicketService ticketService, CDataService cDataService) {
        this.ticketService = ticketService;
        this.cDataService = cDataService;
    }

    /**
     * Get Card Data – GET /cards/{cardId}/{refId}?fields=...
     */
    @GetMapping("/{cardId}/{refId}")
    public ResponseEntity<?> getCardData(@PathVariable String cardId,
                                         @PathVariable String refId,
                                         @RequestParam(required = false) String fields) {
        String cardType = cardTypeFromCardId(cardId);
        if (cardType == null) {
            return ResponseEntity.noContent().build();
        }
        try {
            String cdata = cDataService.buildCData(refId, cardType);
            return ResponseEntity.ok(Map.of("cdata", cdata));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Send Card State – POST /cards/{cardId}/{refId}?cc2=...&event=...
     */
    @PostMapping("/{cardId}/{refId}")
    public ResponseEntity<Void> sendCardState(@PathVariable String cardId,
                                             @PathVariable String refId,
                                             @RequestParam String cc2,
                                             @RequestParam String event,
                                             @RequestBody(required = false) Map<String, String> body) {
        String cardType = cardTypeFromCardId(cardId);
        if (cardType == null) {
            return ResponseEntity.ok().build();
        }
        if ("ticket".equalsIgnoreCase(cardType)) {
            Ticket ticket = ticketService.findByRefId(refId);
            if (ticket != null) {
                switch (event.toUpperCase()) {
                    case "ADDED", "PROVISIONED" -> { /* card added to wallet */ }
                    case "DELETED" -> ticketService.updateStatus(refId, Ticket.TicketStatus.CANCELLED);
                    case "UPDATED" -> { /* optional: sync state */ }
                    default -> { }
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    private String cardTypeFromCardId(String cardId) {
        if (cardId == null) return null;
        if (ticketCardId.equals(cardId)) return "ticket";
        if (boardingPassCardId.equals(cardId)) return "boardingpass";
        if (couponCardId.equals(cardId)) return "coupon";
        if (idCardCardId.equals(cardId)) return "idcard";
        return null;
    }
}
