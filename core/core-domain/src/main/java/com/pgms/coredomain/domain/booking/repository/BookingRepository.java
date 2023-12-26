package com.pgms.coredomain.domain.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.booking.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
