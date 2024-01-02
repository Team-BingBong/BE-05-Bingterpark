package com.pgms.apievent.event.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.pgms.apievent.common.dto.response.PageResponse;
import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventSeatAreaCreateRequest;
import com.pgms.apievent.event.dto.request.EventSeatAreaUpdateRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.event.service.EventService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coreinfraes.document.EventDocument;

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

	@GetMapping("/search/{title}")
	public ResponseEntity<ApiResponse> searchEventByTitle(@PathVariable String title, Pageable pageable) {
		Page<EventDocument> eventDocuments = eventService.searchByTitle(title, pageable);
		return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(eventDocuments)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateEvent(
		@PathVariable Long id,
		@ModelAttribute EventUpdateRequest request) {
		EventResponse response = eventService.updateEvent(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
		eventService.deleteEventById(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> createEventSeatArea(@PathVariable Long id,
		@RequestBody EventSeatAreaCreateRequest request) {
		List<EventSeatAreaResponse> response = eventService.createEventSeatArea(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> getEventSeatAreas(@PathVariable Long id) {
		List<EventSeatAreaResponse> response = eventService.getEventSeatAreas(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> deleteEventSeatArea(@PathVariable Long areaId) {
		eventService.deleteEventSeatArea(areaId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> updateEventSeatArea(@PathVariable Long areaId,
		@RequestBody EventSeatAreaUpdateRequest request) {
		eventService.updateEventSeatArea(areaId, request);
		return ResponseEntity.noContent().build();
	}
}
