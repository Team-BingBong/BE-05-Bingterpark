package com.pgms.apievent.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.image.dto.request.EventImageCreateRequest;
import com.pgms.apievent.image.dto.request.ThumbnailCreateRequest;
import com.pgms.apievent.image.dto.request.ThumbnailUpdateRequest;
import com.pgms.apievent.image.service.EventImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "공연 이미지", description = "공연 이미지 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EventImageController {

	private final EventImageService eventImageService;

	@Operation(summary = "공연 썸네일 이미지 생성", description = "공연 썸네일 이미지 생성 메서드입니다.")
	@PostMapping("/thumbnails/events/{eventId}")
	public ResponseEntity<Void> createEventThumbnail(
		@PathVariable Long eventId,
		@ModelAttribute ThumbnailCreateRequest request) {
		eventImageService.createThumbnail(eventId, request.thumbnail());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "공연 썸네일 이미지 수정", description = "공연 썸네일 이미지 수정 메서드입니다.")
	@PatchMapping("/thumbnails/events/{eventId}")
	public ResponseEntity<Void> updateEventThumbnail(
		@PathVariable Long eventId,
		@ModelAttribute ThumbnailUpdateRequest request) {
		eventImageService.updateThumbnail(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "공연 상세 이미지 생성", description = "공연 상세 이미지 생성 메서드입니다.")
	@PostMapping("/event-images/events/{eventId}")
	public ResponseEntity<Void> addEventImages(
		@PathVariable Long eventId,
		@ModelAttribute EventImageCreateRequest request) {
		eventImageService.addEventDetailImages(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "공연 상세 이미지 삭제", description = "공연 상세 이미지 삭제 메서드입니다.")
	@DeleteMapping("/event-images/events/{eventId}")
	public ResponseEntity<Void> removeEventImages(@PathVariable Long eventId, @RequestParam List<Long> removeImageIds) {
		eventImageService.removeEventImages(eventId, removeImageIds);
		return ResponseEntity.noContent().build();
	}
}
