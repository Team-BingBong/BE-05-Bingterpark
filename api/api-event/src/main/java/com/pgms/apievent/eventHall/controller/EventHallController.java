package com.pgms.apievent.eventHall.controller;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallUpdateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.eventHall.service.EventHallService;
import com.pgms.coredomain.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-halls")
public class EventHallController {

    private final EventHallService eventHallService;

    @PostMapping
    public ResponseEntity<ApiResponse> createEventHall(@RequestBody @Valid EventHallCreateRequest request){
        EventHallResponse eventHallResponse = eventHallService.createEventHall(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventHallResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(ApiResponse.created(eventHallResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventHall(@PathVariable Long id){
        eventHallService.deleteEventHall(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEventHall(
            @PathVariable Long id,
            @RequestBody @Valid EventHallUpdateRequest request) {
        EventHallResponse response = eventHallService.updateEventHall(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEventHall(@PathVariable Long id){
        EventHallResponse response = eventHallService.getEventHall(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getEventHall(){
        List<EventHallResponse> eventHalls = eventHallService.getEventHalls();
        return ResponseEntity.ok(ApiResponse.ok(eventHalls));
    }
}
