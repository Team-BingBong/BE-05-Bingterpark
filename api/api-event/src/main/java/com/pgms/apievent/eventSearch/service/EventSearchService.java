package com.pgms.apievent.eventSearch.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.eventSearch.dto.request.EventKeywordSearchRequest;
import com.pgms.apievent.eventSearch.dto.request.EventSearchRequest;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.pgms.coreinfraes.dto.EventSearchDto;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventSearchService {

	private final EventSearchQueryRepository eventSearchQueryRepository;

	@Transactional(readOnly = true)
	public PageResponseDto searchEvents(EventSearchRequest request) {
		EventSearchDto eventSearchDto = request.toDto();
		Page<EventDocument> eventDocuments = eventSearchQueryRepository.findByCondition(eventSearchDto);
		return PageResponseDto.of(eventDocuments);
	}

	@Transactional(readOnly = true)
	public PageResponseDto searchEventsByKeyword(EventKeywordSearchRequest request) {
		EventKeywordSearchDto eventKeywordSearchDto = request.toDto();
		Page<EventDocument> eventDocuments = eventSearchQueryRepository.findByKeyword(
			eventKeywordSearchDto);
		return PageResponseDto.of(eventDocuments);
	}
}
