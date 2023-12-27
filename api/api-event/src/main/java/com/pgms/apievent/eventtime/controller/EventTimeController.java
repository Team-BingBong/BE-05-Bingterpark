package com.pgms.apievent.eventtime.controller;

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

import com.pgms.apievent.eventtime.dto.request.EventTimeCreateRequest;
import com.pgms.apievent.eventtime.dto.request.EventTimeUpdateRequest;
import com.pgms.apievent.eventtime.dto.response.EventTimeResponse;
import com.pgms.apievent.eventtime.service.EventTimeService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-times")
public class EventTimeController {

	private final EventTimeService eventTimeService;

	@PostMapping("/{eventId}")
	public ResponseEntity<ApiResponse> createEventTimeForEvent(
		@PathVariable Long eventId,
		@Valid @RequestBody EventTimeCreateRequest request) {
		EventTimeResponse response = eventTimeService.createEventTime(eventId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@GetMapping("/{eventTimeId}")
	public ResponseEntity<ApiResponse> getEventTimeForEventById(@PathVariable Long eventTimeId) {
		EventTimeResponse response = eventTimeService.getEventTimeById(eventTimeId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/events/{eventId}")
	public ResponseEntity<ApiResponse> getEventTimesByEventId(@PathVariable Long eventId) {
		List<EventTimeResponse> response = eventTimeService.getEventTimesByEventId(eventId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@PatchMapping("/{eventTimeId}")
	public ResponseEntity<ApiResponse> updateEventTimeForEvent(
		@PathVariable Long eventTimeId,
		@Valid @RequestBody EventTimeUpdateRequest request) {
		EventTimeResponse response = eventTimeService.updateEventTime(eventTimeId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/{eventTimeId}")
	public ResponseEntity<Void> deleteEventTimeForEvent(@PathVariable Long eventTimeId) {
		eventTimeService.deleteEventTimeById(eventTimeId);
		return ResponseEntity.noContent().build();
	}
}
