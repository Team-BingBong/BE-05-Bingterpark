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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EventImageController {

	private final EventImageService eventImageService;

	@PostMapping("/thumbnails/events/{eventId}")
	public ResponseEntity<Void> createEventThumbnail(
		@PathVariable Long eventId,
		@ModelAttribute ThumbnailCreateRequest request) {
		eventImageService.createThumbnail(eventId, request.thumbnail());
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/thumbnails/events/{eventId}")
	public ResponseEntity<Void> updateEventThumbnail(
		@PathVariable Long eventId,
		@ModelAttribute ThumbnailUpdateRequest request) {
		eventImageService.updateThumbnail(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/event-images/events/{eventId}")
	public ResponseEntity<Void> addEventImages(
		@PathVariable Long eventId,
		@ModelAttribute EventImageCreateRequest request) {
		eventImageService.addEventDetailImages(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/event-images/events/{eventId}")
	public ResponseEntity<Void> removeEventImages(@PathVariable Long eventId, @RequestParam List<Long> removeImageIds) {
		eventImageService.removeEventImages(eventId, removeImageIds);
		return ResponseEntity.noContent().build();
	}
}
