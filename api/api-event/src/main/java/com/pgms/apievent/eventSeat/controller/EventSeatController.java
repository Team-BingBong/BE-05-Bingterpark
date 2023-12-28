package com.pgms.apievent.eventSeat.controller;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
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

    private EventSeatService eventSeatService;

    @PostMapping("/seats/events/{id}")
    public ResponseEntity<Void> createEventSeats(@PathVariable Long id,
                                                 @RequestBody EventSeatsCreateRequest eventSeatsCreateRequest){
        eventSeatService.createEventSeats(id, eventSeatsCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/seats/seat-area")
    public ResponseEntity<Void> updateEventSeatSeatArea(@RequestParam List<Long> ids,
                                                        @RequestParam(value = "seat-area-id") Long seatAreaId) {
        eventSeatService.updateEventSeatsSeatArea(ids, seatAreaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/seats/seat-status")
    public ResponseEntity<Void> updateEventSeatSeatStatus(@RequestParam List<Long> ids,
                                                          @RequestParam(value = "seat-status") EventSeatStatus eventSeatStatus) {
        eventSeatService.updateEventSeatsStatus(ids, eventSeatStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/seats")
    public ResponseEntity<Void> deleteEventSeats(@RequestParam List<Long> ids) {
        eventSeatService.deleteEventSeats(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event-time/{id}/seats")
    public ResponseEntity<ApiResponse> getEventSeatsByEventTime(@PathVariable Long id){
        List<EventSeatResponse> responses = eventSeatService.getEventSeatsByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }
}
