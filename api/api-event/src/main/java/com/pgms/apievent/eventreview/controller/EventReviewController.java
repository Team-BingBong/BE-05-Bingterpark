package com.pgms.apievent.eventreview.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.apievent.eventreview.dto.request.EventReviewCreateRequest;
import com.pgms.apievent.eventreview.dto.request.EventReviewUpdateRequest;
import com.pgms.apievent.eventreview.dto.response.EventReviewResponse;
import com.pgms.apievent.eventreview.service.EventReviewService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "공연 후기", description = "공연 후기 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-reviews")
public class EventReviewController {

	private final EventReviewService eventReviewService;

	@Operation(summary = "공연 후기 생성", description = "공연 후기 생성 메서드입니다.")
	@PostMapping("/{eventId}")
	public ResponseEntity<ApiResponse> createEventReview(
		@CurrentAccount Long memberId,
		@PathVariable Long eventId,
		@Valid @RequestBody EventReviewCreateRequest request) {
		EventReviewResponse response = eventReviewService.createEventReview(memberId, eventId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();

		return ResponseEntity.created(location).body(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 후기 수정", description = "공연 후기 수정 메서드입니다.")
	@PatchMapping("/{reviewId}")
	public ResponseEntity<ApiResponse> updateEventReview(
		@CurrentAccount Long memberId,
		@PathVariable Long reviewId,
		@Valid @RequestBody EventReviewUpdateRequest request) {
		EventReviewResponse response = eventReviewService.updateEventReview(memberId, reviewId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 후기 조회 - ID", description = "공연 후기 ID로 공연 후기를 조회하는 메서드입니다.")
	@GetMapping("/{reviewId}")
	public ResponseEntity<ApiResponse> getEventReviewById(@PathVariable Long reviewId) {
		EventReviewResponse response = eventReviewService.getEventReviewById(reviewId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 후기 조회 - 특정 공연", description = "특정 공연에 대한 전체 후기를 조회하는 메서드입니다.")
	@GetMapping("events/{eventId}")
	public ResponseEntity<ApiResponse> getEventReviewsForEventByEventId(@PathVariable Long eventId) {
		List<EventReviewResponse> responses = eventReviewService.getEventReviewsForEventByEventId(eventId);
		return ResponseEntity.ok(ApiResponse.ok(responses));
	}

	@Operation(summary = "공연 후기 삭제", description = "후기를 삭제하는 메서드입니다.")
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteEventReviewById(@CurrentAccount Long memberId, @PathVariable Long reviewId) {
		eventReviewService.deleteEventReviewById(memberId, reviewId);
		return ResponseEntity.noContent().build();
	}
}
