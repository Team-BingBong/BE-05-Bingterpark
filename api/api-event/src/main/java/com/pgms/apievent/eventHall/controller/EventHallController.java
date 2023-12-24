package com.pgms.apievent.eventHall.controller;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallEditRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.eventHall.service.EventHallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EventHallController {

    private final EventHallService eventHallService;

    @PostMapping("/backoffice/event-halls")
    public void createEventHall(@RequestBody EventHallCreateRequest eventHallCreateRequest){
        EventHallResponse eventHallResponse = eventHallService.createEventHall(eventHallCreateRequest);
    }

    @DeleteMapping("/backoffice/event-halls/{id}")
    public void deleteEventHall(@PathVariable Long id){
        eventHallService.deleteEventHall(id);
    }

    @PutMapping("/backoffice/event-halls/{id}")
    public void editEventHall(@PathVariable Long id, @RequestBody EventHallEditRequest eventHallEditRequest){
        eventHallService.editEventHall(id, eventHallEditRequest);
    }
}
