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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "공연 회차", description = "공연 회차 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-times")
public class EventTimeController {

	private final EventTimeService eventTimeService;

	@Operation(summary = "공연 회차 생성", description = "공연 회차 생성 메서드입니다.")
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

	@Operation(summary = "공연 회차 조회 - ID", description = "공연 회차 ID로 공연 회차를 조회하는 메서드입니다.")
	@GetMapping("/{eventTimeId}")
	public ResponseEntity<ApiResponse> getEventTimeForEventById(@PathVariable Long eventTimeId) {
		EventTimeResponse response = eventTimeService.getEventTimeById(eventTimeId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 회차 조회 - 특정 공연", description = "특정 공연에 대한 공연 회차 목록을 조회하는 메서드입니다.")
	@GetMapping("/events/{eventId}")
	public ResponseEntity<ApiResponse> getEventTimesByEventId(@PathVariable Long eventId) {
		List<EventTimeResponse> response = eventTimeService.getEventTimesByEventId(eventId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 회차 수정", description = "공연 회차를 수정하는 메서드입니다.")
	@PatchMapping("/{eventTimeId}")
	public ResponseEntity<ApiResponse> updateEventTimeForEvent(
		@PathVariable Long eventTimeId,
		@Valid @RequestBody EventTimeUpdateRequest request) {
		EventTimeResponse response = eventTimeService.updateEventTime(eventTimeId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 회차 삭제", description = "공연 회차를 삭제하는 메서드입니다.")
	@DeleteMapping("/{eventTimeId}")
	public ResponseEntity<Void> deleteEventTimeForEvent(@PathVariable Long eventTimeId) {
		eventTimeService.deleteEventTimeById(eventTimeId);
		return ResponseEntity.noContent().build();
	}
}
