package com.pgms.apievent.eventSearch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.aop.LogExecutionTime;
import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventKeywordSearchRequest;
import com.pgms.apievent.eventSearch.dto.response.RecentTop10KeywordsResponse;
import com.pgms.coreinfraes.dto.EventDocumentResponse;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.TopTenSearchResponse;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventSearchService {

	private final EventSearchQueryRepository eventSearchQueryRepository;

	@Transactional(readOnly = true)
	@LogExecutionTime
	public PageResponseDto searchEventsByKeyword(EventKeywordSearchRequest request) {
		EventKeywordSearchDto eventKeywordSearchDto = request.toDto();
		Page<EventDocumentResponse> eventDocuments = eventSearchQueryRepository.findByKeyword(eventKeywordSearchDto);
		return PageResponseDto.of(eventDocuments);
	}

	@Transactional(readOnly = true)
	public List<RecentTop10KeywordsResponse> getRecentTop10Keywords() {
		List<TopTenSearchResponse> recentTop10Keywords = eventSearchQueryRepository.getRecentTop10Keywords();
		return recentTop10Keywords.stream().map(RecentTop10KeywordsResponse::of).toList();
	}
}
