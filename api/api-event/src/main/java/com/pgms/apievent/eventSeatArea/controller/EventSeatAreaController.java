package com.pgms.apievent.eventSeatArea.controller;

import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaCreateRequest;
import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaUpdateRequest;
import com.pgms.apievent.eventSeatArea.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.eventSeatArea.service.EventSeatAreaService;
import com.pgms.coredomain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공연 좌석 구역", description = "공연 좌석 구역 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventSeatAreaController {

	private final EventSeatAreaService eventSeatAreaService;

	@Operation(summary = "공연 좌석 구역 생성", description = "특정 공연의 좌석 구역을 생성합니다.")
	@PostMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> createEventSeatArea(
		@PathVariable Long id,
		@RequestBody @Valid EventSeatAreaCreateRequest request) {
		List<EventSeatAreaResponse> response = eventSeatAreaService.createEventSeatArea(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 좌석 구역 조회", description = "특정 공연의 좌석 구역을 모두 조회합니다.")
	@GetMapping("/{id}/seat-area")
	public ResponseEntity<ApiResponse> getEventSeatAreas(@PathVariable Long id) {
		List<EventSeatAreaResponse> response = eventSeatAreaService.getEventSeatAreas(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 좌석 구역 삭제", description = "특정 공연의 좌석 구역을 삭제합니다.")
	@DeleteMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> deleteEventSeatArea(@PathVariable Long areaId) {
		eventSeatAreaService.deleteEventSeatArea(areaId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "공연 좌석 구역 수정", description = "특정 공연의 좌석 구역을 수정합니다.")
	@PutMapping("/seat-area/{areaId}")
	public ResponseEntity<Void> updateEventSeatArea(
		@PathVariable Long areaId,
		@RequestBody @Valid EventSeatAreaUpdateRequest request) {
		eventSeatAreaService.updateEventSeatArea(areaId, request);
		return ResponseEntity.noContent().build();
	}
}
