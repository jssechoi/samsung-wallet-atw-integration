package com.samsung.wallet.atw.demo.service;

import com.samsung.wallet.atw.crypto.WalletCryptoConfig;
import com.samsung.wallet.atw.crypto.WalletCryptoUtil;
import com.samsung.wallet.atw.demo.entity.BoardingPass;
import com.samsung.wallet.atw.demo.entity.Coupon;
import com.samsung.wallet.atw.demo.entity.IdCard;
import com.samsung.wallet.atw.demo.entity.Ticket;
import org.springframework.stereotype.Service;

/**
 * Builds cdata (and pdata refId) for all supported card types: ticket, boardingpass, coupon, idcard.
 */
@Service
public class CDataService {

    private final WalletCryptoUtil util;
    private final TicketService ticketService;
    private final BoardingPassService boardingPassService;
    private final CouponService couponService;
    private final IdCardService idCardService;

    public CDataService(WalletCryptoConfig config,
                        TicketService ticketService,
                        BoardingPassService boardingPassService,
                        CouponService couponService,
                        IdCardService idCardService) {
        this.util = new WalletCryptoUtil(config);
        this.ticketService = ticketService;
        this.boardingPassService = boardingPassService;
        this.couponService = couponService;
        this.idCardService = idCardService;
    }

    public String buildCData(Ticket ticket) throws Exception {
        String cardJson = CardPayloadBuilder.toCardPayloadJson(ticket);
        return util.buildCData(cardJson);
    }

    /** Build cdata for any card type. cardType: ticket, boardingpass, coupon, idcard. */
    public String buildCData(String refId, String cardType) throws Exception {
        String cardJson = switch (cardType == null || cardType.isBlank() ? "ticket" : cardType.toLowerCase()) {
            case "boardingpass" -> {
                BoardingPass bp = boardingPassService.findByRefId(refId);
                if (bp == null) throw new IllegalArgumentException("Boarding pass not found: " + refId);
                yield CardPayloadBuilder.toBoardingPassPayloadJson(bp);
            }
            case "coupon" -> {
                Coupon c = couponService.findByRefId(refId);
                if (c == null) throw new IllegalArgumentException("Coupon not found: " + refId);
                yield CardPayloadBuilder.toCouponPayloadJson(c);
            }
            case "idcard" -> {
                IdCard id = idCardService.findByRefId(refId);
                if (id == null) throw new IllegalArgumentException("Id card not found: " + refId);
                yield CardPayloadBuilder.toIdCardPayloadJson(id);
            }
            default -> {
                Ticket ticket = ticketService.findByRefId(refId);
                if (ticket == null) throw new IllegalArgumentException("Ticket not found: " + refId);
                yield CardPayloadBuilder.toCardPayloadJson(ticket);
            }
        };
        return util.buildCData(cardJson);
    }

    public String buildPDataRefId(String refId) {
        return util.buildPDataRefId(refId);
    }
}
