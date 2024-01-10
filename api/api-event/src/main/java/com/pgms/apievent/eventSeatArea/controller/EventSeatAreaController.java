package com.pgms.apievent.eventSeatArea.controller;

import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaCreateRequest;
import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaUpdateRequest;
import com.pgms.apievent.eventSeatArea.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.eventSeatArea.service.EventSeatAreaService;
import com.pgms.coredomain.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventSeatAreaController {

	private final EventSeatAreaService eventSeatAreaService;

	@PostMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> createEventSeatArea(
		@PathVariable Long id,
		@RequestBody @Valid EventSeatAreaCreateRequest request) {
		List<EventSeatAreaResponse> response = eventSeatAreaService.createEventSeatArea(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> getEventSeatAreas(@PathVariable Long id) {
		List<EventSeatAreaResponse> response = eventSeatAreaService.getEventSeatAreas(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@DeleteMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> deleteEventSeatArea(@PathVariable Long areaId) {
		eventSeatAreaService.deleteEventSeatArea(areaId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> updateEventSeatArea(
		@PathVariable Long areaId,
		@RequestBody @Valid EventSeatAreaUpdateRequest request) {
		eventSeatAreaService.updateEventSeatArea(areaId, request);
		return ResponseEntity.noContent().build();
	}
}
