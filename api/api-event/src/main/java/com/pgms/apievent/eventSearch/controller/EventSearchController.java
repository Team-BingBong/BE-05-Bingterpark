package com.pgms.apievent.eventSearch.controller;

import com.pgms.coreinfraes.dto.request.EventSearchRequest;
import com.pgms.apievent.eventSearch.service.EventSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events/search")
@RequiredArgsConstructor
public class EventSearchController {

    private final EventSearchService eventSearchService;

    @GetMapping
    public void searchEvents(@ModelAttribute EventSearchRequest eventSearchRequest){

        eventSearchService.searchEvents(eventSearchRequest);
    }

}
