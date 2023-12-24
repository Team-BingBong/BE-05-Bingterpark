package com.pgms.coredomain.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
