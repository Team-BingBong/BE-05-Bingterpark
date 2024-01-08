package com.pgms.apibooking.domain.booking.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.pgms.apibooking.domain.booking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.domain.booking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.domain.booking.service.BookingService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController { //TODO: 인증된 멤버 연동

	private final BookingService bookingService;

	@PostMapping
	public ResponseEntity<ApiResponse<BookingCreateResponse>> createBooking(
		//@CurrentAccount Long memberId,
		@RequestBody @Valid BookingCreateRequest request,
		HttpServletRequest httpRequest) {
		BookingCreateResponse createdBooking = bookingService.createBooking(request, 1L);
		ApiResponse<BookingCreateResponse> response = ApiResponse.ok(createdBooking);
		URI location = UriComponentsBuilder
			.fromHttpUrl(httpRequest.getRequestURL().toString())
			.path("/{id}")
			.buildAndExpand(createdBooking.bookingId())
			.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<Void> cancelBooking(
		//@CurrentAccount Long memberId,
		@PathVariable String id,
		@RequestBody @Valid BookingCancelRequest request) {
		bookingService.cancelBooking(id, request, 1L);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/exit")
	public ResponseEntity<Void> exitBooking(@PathVariable String id) {
		bookingService.exitBooking(id);
		return ResponseEntity.ok().build();
	}
}
