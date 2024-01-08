package com.pgms.apibooking.domain.bookingqueue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apibooking.domain.bookingqueue.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.request.BookingQueueExitRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.request.TokenIssueRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.domain.bookingqueue.dto.response.SessionIdIssueResponse;
import com.pgms.apibooking.domain.bookingqueue.dto.response.TokenIssueResponse;
import com.pgms.apibooking.domain.bookingqueue.service.BookingQueueService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingQueueController {

	private final BookingQueueService bookingQueueService;

	@PostMapping("/issue-session-id")
	public ResponseEntity<ApiResponse<SessionIdIssueResponse>> issueSessionId() {
		ApiResponse<SessionIdIssueResponse> response = ApiResponse.ok(bookingQueueService.issueSessionId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/enter-queue")
	public ResponseEntity<Void> enterQueue(@RequestBody @Valid BookingQueueEnterRequest request, HttpServletRequest httpServletRequest) {
		String bookingSessionId = getBookingSessionId(httpServletRequest);
		bookingQueueService.enterQueue(request, bookingSessionId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/order-in-queue")
	public ResponseEntity<ApiResponse<OrderInQueueGetResponse>> getOrderInQueue(@RequestParam Long eventId) {
		ApiResponse<OrderInQueueGetResponse> response =
			ApiResponse.ok(bookingQueueService.getOrderInQueue(eventId, null));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/issue-token")
	public ResponseEntity<ApiResponse<TokenIssueResponse>> issueToken(@RequestBody @Valid TokenIssueRequest request) {
		ApiResponse<TokenIssueResponse> response = ApiResponse.ok(bookingQueueService.issueToken(request, null));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/exit-queue")
	public ResponseEntity<Void> exitQueue(@RequestBody @Valid BookingQueueExitRequest request) {
		bookingQueueService.exitQueue(request, null);
		return ResponseEntity.ok().build();
	}

	private String getBookingSessionId(HttpServletRequest httpServletRequest) {
		return (String) httpServletRequest.getAttribute("bookingSessionId");
	}
}
