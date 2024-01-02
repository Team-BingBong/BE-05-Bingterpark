package com.pgms.apievent.eventSearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventSearchRequest;
import com.pgms.apievent.eventSearch.service.EventSearchService;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events/search")
@RequiredArgsConstructor
public class EventSearchController {

	private final EventSearchService eventSearchService;

	@GetMapping
	public ResponseEntity<ApiResponse> searchEvents(@ModelAttribute EventSearchRequest eventSearchRequest) {
		PageResponseDto response = eventSearchService.searchEvents(eventSearchRequest);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
