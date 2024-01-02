package com.pgms.apievent.eventSearch.service;

import com.pgms.coreinfraes.dto.request.EventSearchRequest;
import com.pgms.coreinfraes.repository.EventSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventSearchService {

    private final EventSearchRepository eventSearchRepository;

    public void searchEvents(EventSearchRequest eventSearchRequest) {

    }
}
