package com.samsung.wallet.atw.demo.partner;

import com.samsung.wallet.atw.demo.entity.Ticket;
import com.samsung.wallet.atw.demo.service.CDataService;
import com.samsung.wallet.atw.demo.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Partner endpoints called by Samsung Wallet server: Get Card Data (GET), Send Card State (POST).
 * All requests under /partner/* are validated by AuthValidationFilter (Authorization Bearer).
 * cardType query param: ticket (default), boardingpass, coupon, idcard.
 */
@RestController
@RequestMapping("/partner/v1/tickets")
public class PartnerWebhookController {

    private final TicketService ticketService;
    private final CDataService cDataService;

    public PartnerWebhookController(TicketService ticketService, CDataService cDataService) {
        this.ticketService = ticketService;
        this.cDataService = cDataService;
    }

    /**
     * Get Card Data – Samsung server calls this to fetch card content by refId.
     * Optional cardType: ticket, boardingpass, coupon, idcard (default ticket).
     */
    @GetMapping("/{refId}")
    public ResponseEntity<?> getCardData(@PathVariable String refId,
                                        @RequestParam(required = false) String fields,
                                        @RequestParam(required = false, defaultValue = "ticket") String cardType) {
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
     * Send Card State – Samsung server notifies partner when user adds/removes card (ADDED, DELETED, etc.).
     * For tickets we update status; other card types just acknowledge.
     */
    @PostMapping("/{refId}/state")
    public ResponseEntity<Void> sendCardState(@PathVariable String refId,
                                              @RequestParam String cc2,
                                              @RequestParam String event,
                                              @RequestParam(required = false, defaultValue = "ticket") String cardType,
                                              @RequestBody(required = false) Map<String, String> body) {
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
}
