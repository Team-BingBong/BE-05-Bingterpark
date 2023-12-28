package com.pgms.coredomain.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.event.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
