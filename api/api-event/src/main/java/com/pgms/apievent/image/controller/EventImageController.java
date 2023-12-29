package com.pgms.apievent.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.image.dto.request.EventImageAddRequest;
import com.pgms.apievent.image.dto.request.EventImageCreateRequest;
import com.pgms.apievent.image.service.EventImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-images")
public class EventImageController {

	private final EventImageService eventImageService;

	@PostMapping("/{eventId}")
	public ResponseEntity<Void> createEventImages(
		@PathVariable Long eventId,
		@ModelAttribute EventImageCreateRequest request) {

		eventImageService.createEventDetailImages(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/modification/{eventId}")
	public ResponseEntity<Void> addEventImages(
		@PathVariable Long eventId,
		@ModelAttribute EventImageAddRequest request) {
		eventImageService.addEventImages(eventId, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/modification/{eventId}")
	public ResponseEntity<Void> removeEventImages(@PathVariable Long eventId, @RequestParam List<Long> removeImageIds) {
		eventImageService.removeEventImages(eventId, removeImageIds);
		return ResponseEntity.noContent().build();
	}
}
