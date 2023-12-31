package com.pgms.apievent.eventSeat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.eventSeat.service.EventSeatService;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-seats")
public class EventSeatController {
  
    private EventSeatService eventSeatService;

    @PostMapping("/events/{id}")
    public ResponseEntity<Void> createEventSeats(@PathVariable Long id,
                                                 @RequestBody List<EventSeatsCreateRequest> eventSeatsCreateRequests){
        eventSeatService.createEventSeats(id, eventSeatsCreateRequests);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/seat-area")
    public ResponseEntity<Void> updateEventSeatSeatArea(@RequestParam List<Long> ids,
                                                        @RequestParam(value = "seat-area-id") Long seatAreaId) {
        eventSeatService.updateEventSeatsSeatArea(ids, seatAreaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/seat-status")
    public ResponseEntity<Void> updateEventSeatSeatStatus(@RequestParam List<Long> ids,
                                                          @RequestParam(value = "seat-status") EventSeatStatus eventSeatStatus) {
        eventSeatService.updateEventSeatsStatus(ids, eventSeatStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEventSeats(@RequestParam List<Long> ids) {
        eventSeatService.deleteEventSeats(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event-time/{id}/seats")
    public ResponseEntity<ApiResponse> getEventSeatsByEventTime(@PathVariable Long id){
        List<EventSeatResponse> responses = eventSeatService.getEventSeatsByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    @GetMapping("/event-time/{id}/availabe-numbers")
    public ResponseEntity<ApiResponse> getLeftEventSeatNumberByEventTime(@PathVariable Long id){
        List<LeftEventSeatResponse> responses = eventSeatService.getLeftEventSeatNumberByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }
}
