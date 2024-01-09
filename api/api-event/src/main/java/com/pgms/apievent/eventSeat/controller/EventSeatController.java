package com.pgms.apievent.eventSeat.controller;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.eventSeat.service.EventSeatService;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-seats")
public class EventSeatController {
  
    private final EventSeatService eventSeatService;

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

    @GetMapping("/event-times/{id}")
    public ResponseEntity<ApiResponse> getEventSeatsByEventTime(@PathVariable Long id){
        List<EventSeatResponse> responses = eventSeatService.getEventSeatsByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    @GetMapping("/event-times/{id}/available-numbers")
    public ResponseEntity<ApiResponse> getLeftEventSeatNumberByEventTime(@PathVariable Long id){
        List<LeftEventSeatResponse> responses = eventSeatService.getLeftEventSeatNumberByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }
}
