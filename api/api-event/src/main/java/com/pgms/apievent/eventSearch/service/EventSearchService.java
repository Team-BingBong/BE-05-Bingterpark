package com.pgms.apievent.eventSearch.service;

import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.dto.request.EventSearchRequest;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSearchService {

    private final EventSearchQueryRepository eventSearchQueryRepository;

    public void searchEvents(EventSearchRequest eventSearchRequest) {

        List<EventDocument> eventDocuments = eventSearchQueryRepository.findByCondition(eventSearchRequest);

    }
}
