package com.pgms.coredomain.domain.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.booking.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
