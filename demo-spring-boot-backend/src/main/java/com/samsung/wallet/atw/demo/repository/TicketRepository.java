package com.samsung.wallet.atw.demo.repository;

import com.samsung.wallet.atw.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findAllByOrderByCreatedAtDesc();
}
