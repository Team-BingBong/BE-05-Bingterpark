package com.pgms.coredomain.domain.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.booking.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT b FROM Booking b JOIN FETCH b.payment WHERE b.id = :id")
	Optional<Booking> findWithPaymentById(@Param("id") String id);
}
