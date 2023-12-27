package com.pgms.apievent.event.controller;

import com.pgms.apievent.event.dto.request.*;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.event.service.EventService;
import com.pgms.coredomain.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

	private final EventService eventService;

	@PostMapping
	public ResponseEntity<ApiResponse> createEvent(@Valid @ModelAttribute EventCreateRequest request) {
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
														   @RequestBody EventSeatAreaCreateRequest request){
		List<EventSeatAreaResponse> response = eventService.createEventSeatArea(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> getEventSeatAreas(@PathVariable Long id){
		List<EventSeatAreaResponse> response = eventService.getEventSeatAreas(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> deleteEventSeatArea(@PathVariable Long areaId){
		eventService.deleteEventSeatArea(areaId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> updateEventSeatArea(@PathVariable Long areaId,
													@RequestBody EventSeatAreaUpdateRequest request){
		eventService.updateEventSeatArea(areaId, request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/events/{id}/seats")
	public ResponseEntity<ApiResponse> createEventSeats(@PathVariable Long id,
								 @RequestBody EventSeatsCreateRequest eventSeatsCreateRequest){
		eventService.createEventSeats(id, eventSeatsCreateRequest);
		return ResponseEntity.ok(ApiResponse.ok(null));
	}
}
