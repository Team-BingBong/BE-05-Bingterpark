package com.pgms.coredomain.domain.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.booking.BookingCancel;

public interface BookingCancelRepository extends JpaRepository<BookingCancel, Long> {
}
