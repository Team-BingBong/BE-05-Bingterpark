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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "공연", description = "공연 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

	private final EventService eventService;

	@Operation(summary = "공연 생성", description = "공연 생성 메서드입니다.")
	@PostMapping
	public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody EventCreateRequest request) {
		EventResponse response = eventService.createEvent(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@Operation(summary = "공연ID 조회", description = "공연을 ID로 조회하는 메서드입니다.")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getEventById(@PathVariable Long id) {
		EventResponse response = eventService.getEventById(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 목록 조회 - 랭킹순", description = "공연 목록을 예매가 많은 순서로 조회하는 메서드입니다.")
	@GetMapping("/sort/ranking")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByRanking(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByRanking(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 목록 조회 - 리뷰순", description = "공연 목록을 후기가 많은 순서로 조회하는 메서드입니다.")
	@GetMapping("/sort/review")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByReview(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByReview(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 목록 조회 - 마감임박순", description = "공연 목록을 예매 마감일 임박 순서로 조회하는 메서드입니다.")
	@GetMapping("/sort/ended-at")
	public ResponseEntity<ApiResponse> getEventsPageByGenreSortedByBookingEndedAt(
		@Valid @ModelAttribute EventPageRequest request) {
		PageResponseDto response = eventService.getEventsPageByGenreSortedByBookingEndedAt(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 정보 수정", description = "공연 정보를 수정하는 메서드입니다.")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateEvent(
		@PathVariable Long id,
		@RequestBody EventUpdateRequest request) {
		EventResponse response = eventService.updateEvent(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "공연 삭제", description = "공연을 ID로 삭제하는 메서드입니다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
		eventService.deleteEventById(id);
		return ResponseEntity.noContent().build();
	}
}
