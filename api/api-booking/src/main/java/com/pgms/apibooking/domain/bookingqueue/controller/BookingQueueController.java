package com.pgms.apibooking.domain.bookingqueue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "예매 대기열")
public class BookingQueueController {

	private final BookingQueueService bookingQueueService;

	@Operation(summary = "세션 아이디 발급")
	@PostMapping("/issue-session-id")
	public ResponseEntity<ApiResponse<SessionIdIssueResponse>> issueSessionId() {
		ApiResponse<SessionIdIssueResponse> response = ApiResponse.ok(bookingQueueService.issueSessionId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "대기열 진입")
	@PostMapping("/enter-queue")
	public ResponseEntity<Void> enterWaitingQueue(@RequestBody @Valid BookingQueueEnterRequest request, @RequestAttribute("bookingSessionId") String bookingSessionId) {
		bookingQueueService.enterWaitingQueue(request, bookingSessionId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "내 대기 순서 확인")
	@GetMapping("/waiting-order")
	public ResponseEntity<ApiResponse<OrderInQueueGetResponse>> getWaitingOrder(@RequestParam Long eventId, @RequestAttribute("bookingSessionId") String bookingSessionId) {
		ApiResponse<OrderInQueueGetResponse> response =
			ApiResponse.ok(bookingQueueService.getWaitingOrder(eventId, bookingSessionId));
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "예매 토큰 발급")
	@PostMapping("/issue-token")
	public ResponseEntity<ApiResponse<TokenIssueResponse>> issueToken(@RequestBody @Valid TokenIssueRequest request, @RequestAttribute("bookingSessionId") String bookingSessionId) {
		ApiResponse<TokenIssueResponse> response = ApiResponse.ok(bookingQueueService.issueToken(request, bookingSessionId));
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "대기열 이탈")
	@PostMapping("/exit-queue")
	public ResponseEntity<Void> exitQueue(@RequestBody @Valid BookingQueueExitRequest request, @RequestAttribute("bookingSessionId") String bookingSessionId) {
		bookingQueueService.exitQueue(request, bookingSessionId);
		return ResponseEntity.noContent().build();
	}
}
