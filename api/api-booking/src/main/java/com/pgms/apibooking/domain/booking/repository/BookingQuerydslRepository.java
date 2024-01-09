package com.pgms.apibooking.domain.booking.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.pgms.apibooking.domain.booking.dto.request.BookingSearchCondition;
import com.pgms.coredomain.domain.booking.Booking;

public interface BookingQuerydslRepository {

	List<Booking> findAll(BookingSearchCondition condition, Pageable pageable);

	Long count(BookingSearchCondition condition);
}
