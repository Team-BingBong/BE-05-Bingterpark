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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-reviews")
public class EventReviewController {

	private final EventReviewService eventReviewService;

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

	@PatchMapping("/{reviewId}")
	public ResponseEntity<ApiResponse> updateEventReview(
		@CurrentAccount Long memberId,
		@PathVariable Long reviewId,
		@Valid @RequestBody EventReviewUpdateRequest request) {
		EventReviewResponse response = eventReviewService.updateEventReview(memberId, reviewId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/{reviewId}")
	public ResponseEntity<ApiResponse> getEventReviewById(@PathVariable Long reviewId) {
		EventReviewResponse response = eventReviewService.getEventReviewById(reviewId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("events/{eventId}")
	public ResponseEntity<ApiResponse> getEventReviewsForEventByEventId(@PathVariable Long eventId) {
		List<EventReviewResponse> responses = eventReviewService.getEventReviewsForEventByEventId(eventId);
		return ResponseEntity.ok(ApiResponse.ok(responses));
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteEventReviewById(@CurrentAccount Long memberId, @PathVariable Long reviewId) {
		eventReviewService.deleteEventReviewById(memberId, reviewId);
		return ResponseEntity.noContent().build();
	}
}
