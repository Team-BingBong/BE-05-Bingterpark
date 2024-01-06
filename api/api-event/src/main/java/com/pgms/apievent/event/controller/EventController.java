package com.pgms.apievent.event.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventPageRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.service.EventService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

	private final EventService eventService;

	@PostMapping
	public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody EventCreateRequest request) {
		EventResponse response = eventService.createEvent(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getEventById(@PathVariable Long id) {
		EventResponse response = eventService.getEventById(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/sort/ranking")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByRanking(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByRanking(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/sort/review")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByReview(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByReview(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/sort/ended-at")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByBookingEndedAt(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByBookingEndedAt(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateEvent(
		@PathVariable Long id,
		@RequestBody EventUpdateRequest request) {
		EventResponse response = eventService.updateEvent(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
		eventService.deleteEventById(id);
		return ResponseEntity.noContent().build();
	}
}
