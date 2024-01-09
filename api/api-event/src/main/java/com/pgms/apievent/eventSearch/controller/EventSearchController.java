package com.pgms.apievent.eventSearch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventKeywordSearchRequest;
import com.pgms.apievent.eventSearch.dto.response.RecentTop10KeywordsResponse;
import com.pgms.apievent.eventSearch.service.EventSearchService;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events/search")
@RequiredArgsConstructor
public class EventSearchController {

	private final EventSearchService eventSearchService;

	@GetMapping("/keyword")
	public ResponseEntity<ApiResponse> searchEventsByKeyword(
		@ModelAttribute EventKeywordSearchRequest eventKeywordSearchRequest) {
		PageResponseDto response = eventSearchService.searchEventsByKeyword(eventKeywordSearchRequest);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/top-ten")
	ResponseEntity<ApiResponse> getRecentTop10Keywords() {
		List<RecentTop10KeywordsResponse> response = eventSearchService.getRecentTop10Keywords();
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
