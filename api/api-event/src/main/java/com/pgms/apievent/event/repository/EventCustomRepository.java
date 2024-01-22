package com.pgms.apievent.event.repository;

import org.springframework.data.domain.Page;

import com.pgms.apievent.event.dto.request.EventPageRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;

public interface EventCustomRepository {

	Page<EventResponse> getEventsPageByGenreSortedByRanking(EventPageRequest eventPageRequest);

	Page<EventResponse> getEventsPageByGenreSortedByReview(EventPageRequest eventPageRequest);

	Page<EventResponse> getEventsPageByGenreSortedByBookingEndedAt(EventPageRequest eventPageRequest);

	Page<EventResponse> getEventsByKeyword(EventKeywordSearchDto eventKeywordSearchDto);
}
