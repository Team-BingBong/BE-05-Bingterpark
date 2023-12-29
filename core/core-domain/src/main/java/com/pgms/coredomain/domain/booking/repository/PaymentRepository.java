package com.pgms.coredomain.domain.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.booking.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByBookingId(String bookingId);

	Optional<Payment> findByPaymentKey(String paymentKey);
}
