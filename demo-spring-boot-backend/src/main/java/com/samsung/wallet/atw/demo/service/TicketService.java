package com.samsung.wallet.atw.demo.service;

import com.samsung.wallet.atw.demo.entity.Ticket;
import com.samsung.wallet.atw.demo.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Ticket findByRefId(String refId) {
        return repository.findById(refId).orElse(null);
    }

    public Ticket save(Ticket ticket) {
        return repository.save(ticket);
    }

    public void updateStatus(String refId, Ticket.TicketStatus status) {
        Optional<Ticket> opt = repository.findById(refId);
        opt.ifPresent(t -> {
            t.setStatus(status);
            t.setUpdatedAt(java.time.Instant.now());
            repository.save(t);
        });
    }
}
