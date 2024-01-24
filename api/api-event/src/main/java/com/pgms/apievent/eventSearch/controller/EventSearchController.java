package com.pgms.apievent.eventSearch.controller;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventKeywordSearchRequest;
import com.pgms.apievent.eventSearch.dto.response.RecentTop10KeywordsResponse;
import com.pgms.apievent.eventSearch.service.EventSearchService;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "검색", description = "검색 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/events/search")
@RequiredArgsConstructor
public class EventSearchController {

	private final EventSearchService eventSearchService;

	@Operation(summary = "키워드 검색", description = "키워드로 종합 검색합니다.")
	@GetMapping("/keyword")
	public ResponseEntity<ApiResponse> searchEventsByKeyword(
		@ModelAttribute @Valid EventKeywordSearchRequest eventKeywordSearchRequest,
		BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			throw new EventException(BINDING_FAILED_EXCEPTION);
		}

		PageResponseDto response = eventSearchService.searchEventsByKeyword(eventKeywordSearchRequest);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "실시간 검색", description = "실시간 인기 검색어 10개를 조회합니다.")
	@GetMapping("/top-ten")
	ResponseEntity<ApiResponse> getRecentTop10Keywords() {
		List<RecentTop10KeywordsResponse> response = eventSearchService.getRecentTop10Keywords();
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
