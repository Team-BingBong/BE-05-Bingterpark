package com.pgms.apievent.eventSearch.service;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventKeywordSearchRequest;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.TopTenSearchResponse;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSearchService {

	private final EventSearchQueryRepository eventSearchQueryRepository;

	@Transactional(readOnly = true)
	public PageResponseDto searchEventsByKeyword(EventKeywordSearchRequest request) {
		EventKeywordSearchDto eventKeywordSearchDto = request.toDto();
		Page<EventDocument> eventDocuments = eventSearchQueryRepository.findByKeyword(
			eventKeywordSearchDto);
		return PageResponseDto.of(eventDocuments);
	}

	@Transactional(readOnly = true)
	public List<TopTenSearchResponse> getRecentTop10Keywords() {
		return eventSearchQueryRepository.getRecentTop10Keywords();
	}
}
