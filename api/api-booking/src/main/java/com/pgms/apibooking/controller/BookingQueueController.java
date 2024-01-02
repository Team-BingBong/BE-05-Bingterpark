package com.pgms.apibooking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apibooking.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.service.BookingQueueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingQueueController {

	private final BookingQueueService bookingQueueService;

	@PostMapping
	public void enterQueue(@RequestBody @Valid BookingQueueEnterRequest request) {
		bookingQueueService.enterBookingQueue(request);
	}
}
