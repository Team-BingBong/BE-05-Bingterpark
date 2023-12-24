package com.pgms.apievent.eventHall.controller;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.eventHall.service.EventHallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventHallController {

    private final EventHallService eventHallService;

    @PostMapping("/backoffice/event-halls")
    public void createEventHall(EventHallCreateRequest eventHallCreateRequest){
        EventHallResponse eventHallResponse = eventHallService.createEventHall(eventHallCreateRequest);
    }
}
