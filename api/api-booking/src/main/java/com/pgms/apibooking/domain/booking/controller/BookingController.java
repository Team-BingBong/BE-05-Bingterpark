package com.pgms.apibooking.domain.booking.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pgms.apibooking.domain.booking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingSearchCondition;
import com.pgms.apibooking.domain.booking.dto.request.PageCondition;
import com.pgms.apibooking.domain.booking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.domain.booking.dto.response.BookingGetResponse;
import com.pgms.apibooking.domain.booking.dto.response.BookingsGetResponse;
import com.pgms.apibooking.domain.booking.dto.response.PageResponse;
import com.pgms.apibooking.domain.booking.service.BookingService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "예매")
public class BookingController {

	private final BookingService bookingService;

	@Operation(summary = "예매 생성")
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<BookingCreateResponse>> createBooking(
		@CurrentAccount Long memberId,
		@RequestBody @Valid BookingCreateRequest request,
		@RequestAttribute("tokenSessionId") String tokenSessionId,
		HttpServletRequest httpRequest) {
		BookingCreateResponse createdBooking = bookingService.createBooking(request, memberId, tokenSessionId);
		ApiResponse<BookingCreateResponse> response = ApiResponse.created(createdBooking);
		URI location = UriComponentsBuilder
			.fromHttpUrl(httpRequest.getRequestURL().toString())
			.path("/{id}")
			.buildAndExpand(createdBooking.bookingId())
			.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@Operation(summary = "예매 취소")
	@PostMapping("/{id}/cancel")
	public ResponseEntity<Void> cancelBooking(
		@CurrentAccount Long memberId,
		@PathVariable String id,
		@RequestBody @Valid BookingCancelRequest request) {
		bookingService.cancelBooking(id, request, memberId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "예매 이탈")
	@PostMapping("/{id}/exit")
	public ResponseEntity<Void> exitBooking(@PathVariable String id) {
		bookingService.exitBooking(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "내 예매 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<BookingsGetResponse>>> getBookings(
		@CurrentAccount Long memberId,
		@ModelAttribute @Valid PageCondition pageCondition,
		@ModelAttribute @Valid BookingSearchCondition searchCondition
	) {
		PageResponse<BookingsGetResponse> bookings = bookingService.getBookings(pageCondition, searchCondition, memberId);
		ApiResponse<PageResponse<BookingsGetResponse>> response = ApiResponse.ok(bookings);
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "내 예매 상세 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BookingGetResponse>> getBooking(
		@CurrentAccount Long memberId,
		@PathVariable String id
	) {
		System.out.println("!!!!!!!!! memberId = " + memberId);
		BookingGetResponse booking = bookingService.getBooking(id, memberId);
		ApiResponse<BookingGetResponse> response = ApiResponse.ok(booking);
		return ResponseEntity.ok().body(response);
	}
}
