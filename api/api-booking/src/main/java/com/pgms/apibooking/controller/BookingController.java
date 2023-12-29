package com.pgms.apibooking.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.service.BookingService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<ApiResponse<BookingCreateResponse>> createBooking(
		@RequestBody @Valid BookingCreateRequest request) {
		BookingCreateResponse response = bookingService.createBooking(request);
		URI location = UriComponentsBuilder
			.fromPath("/api/v1/bookings/{bookingId}")
			.buildAndExpand(response.bookingId())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}
}
