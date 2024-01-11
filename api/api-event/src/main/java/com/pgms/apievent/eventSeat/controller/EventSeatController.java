package com.pgms.apievent.eventSeat.controller;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.eventSeat.service.EventSeatService;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공연 좌석", description = "공연 좌석 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-seats")
public class EventSeatController {
  
    private final EventSeatService eventSeatService;

    @Operation(summary = "공연 좌석 생성", description = "특정 공연의 좌석을 생성합니다.")
    @PostMapping("/events/{id}")
    public ResponseEntity<Void> createEventSeats(@PathVariable Long id,
                                                 @RequestBody List<EventSeatsCreateRequest> eventSeatsCreateRequests){
        eventSeatService.createEventSeats(id, eventSeatsCreateRequests);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연 좌석 구역 수정", description = "지정 좌석들의 구역을 일괄 수정합니다.")
    @PatchMapping("/seat-area")
    public ResponseEntity<Void> updateEventSeatSeatArea(@RequestParam List<Long> ids,
                                                        @RequestParam(value = "seat-area-id") Long seatAreaId) {
        eventSeatService.updateEventSeatsSeatArea(ids, seatAreaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연 좌석 상태 수정", description = "지정 좌석들의 상태를 일괄 수정합니다.")
    @PatchMapping("/seat-status")
    public ResponseEntity<Void> updateEventSeatSeatStatus(@RequestParam List<Long> ids,
                                                          @RequestParam(value = "seat-status") EventSeatStatus eventSeatStatus) {
        eventSeatService.updateEventSeatsStatus(ids, eventSeatStatus);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연 좌석 삭제", description = "지정 좌석들을 일괄 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deleteEventSeats(@RequestParam List<Long> ids) {
        eventSeatService.deleteEventSeats(ids);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연 회차별 좌석 조회", description = "특정 공연 회차의 좌석을 모두 조회합니다.")
    @GetMapping("/event-times/{id}")
    public ResponseEntity<ApiResponse> getEventSeatsByEventTime(@PathVariable Long id){
        List<EventSeatResponse> responses = eventSeatService.getEventSeatsByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    @Operation(summary = "공연 회차 좌석 구역별 남은 자리수 조회", description = "특정 공연 회차의 좌석 구역별 남은 자리수를 조회합니다.")
    @GetMapping("/event-times/{id}/available-numbers")
    public ResponseEntity<ApiResponse> getLeftEventSeatNumberByEventTime(@PathVariable Long id){
        List<LeftEventSeatResponse> responses = eventSeatService.getLeftEventSeatNumberByEventTime(id);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }
}
