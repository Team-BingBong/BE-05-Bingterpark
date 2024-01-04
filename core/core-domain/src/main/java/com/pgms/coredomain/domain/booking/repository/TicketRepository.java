package com.pgms.coredomain.domain.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.booking.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("SELECT t FROM Ticket t WHERE t.booking.id = :bookingId")
	List<Ticket> findAllByBookingId(@Param("bookingId") String bookingId);
}
