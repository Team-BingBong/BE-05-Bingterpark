package com.pgms.coredomain.domain.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.event.EventSeatStatus;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("SELECT t FROM Ticket t WHERE t.booking.id = :bookingId")
	List<Ticket> findAllByBookingId(@Param("bookingId") String bookingId);

	@Query("SELECT tk FROM Ticket tk "
		+ "LEFT JOIN FETCH tk.booking b "
		+ "LEFT JOIN FETCH b.time tm "
		+ "LEFT JOIN FETCH tm.event e "
		+ "WHERE :datetime BETWEEN e.bookingStartedAt AND e.bookingEndedAt "
		+ "AND b.status = :bookingStatus ")
	List<Ticket> findAll(@Param("datetime") LocalDateTime dateTime,
		@Param("bookingStatus") BookingStatus bookingStatus);

	@Modifying
	@Query("UPDATE Ticket t SET t.seat.status = :status WHERE t IN :ticketIds")
	void updateSeatStatusBulk(@Param("status") EventSeatStatus status, @Param("ticketIds") List<Long> ticketIds);
}
