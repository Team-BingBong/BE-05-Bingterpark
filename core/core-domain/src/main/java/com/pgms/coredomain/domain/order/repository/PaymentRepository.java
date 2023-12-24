package com.pgms.coredomain.domain.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.order.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByOrderId(Long orderId);
}
