package com.pgms.apievent.eventHall.controller;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallUpdateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.eventHall.service.EventHallService;
import com.pgms.coredomain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "공연장", description = "공연장 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-halls")
public class EventHallController {

    private final EventHallService eventHallService;

    @Operation(summary = "공연장 생성", description = "공연장을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse> createEventHall(@RequestBody @Valid EventHallCreateRequest request){
        EventHallResponse eventHallResponse = eventHallService.createEventHall(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventHallResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(ApiResponse.created(eventHallResponse));
    }

    @Operation(summary = "공연장 생성", description = "공연장을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventHall(@PathVariable Long id){
        eventHallService.deleteEventHall(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공연장 수정", description = "공연장 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEventHall(
            @PathVariable Long id,
            @RequestBody @Valid EventHallUpdateRequest request) {
        EventHallResponse response = eventHallService.updateEventHall(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "공연장 단일 조회", description = "공연장 한개의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEventHall(@PathVariable Long id){
        EventHallResponse response = eventHallService.getEventHall(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "공연장 일괄 조회", description = "모든 공연장 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse> getEventHall(){
        List<EventHallResponse> eventHalls = eventHallService.getEventHalls();
        return ResponseEntity.ok(ApiResponse.ok(eventHalls));
    }
}
