package com.pgms.apibooking.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.pgms.apibooking.fake.TossPaymentServiceFake;
import com.pgms.apibooking.service.TossPaymentService;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

@TestConfiguration
public class TestConfig {

	@Bean
	public TossPaymentService tossPaymentService(BookingRepository bookingRepository,
		PaymentRepository paymentRepository) {
		return new TossPaymentServiceFake(bookingRepository, paymentRepository);
	}
}
