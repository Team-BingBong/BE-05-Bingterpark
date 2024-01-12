package com.pgms.apibooking.domain.seat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apibooking.domain.seat.dto.request.SeatsGetRequest;
import com.pgms.apibooking.domain.seat.dto.response.AreaResponse;
import com.pgms.apibooking.domain.seat.service.SeatService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
@Tag(name = "좌석")
public class SeatController {

	private final SeatService seatService;

	@Operation(summary = "좌석 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<List<AreaResponse>>> getSeats(@ModelAttribute @Valid SeatsGetRequest request) {
		ApiResponse<List<AreaResponse>> response = ApiResponse.ok(seatService.getSeats(request));
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "좌석 선택")
	@PostMapping("/{seatId}/select")
	public ResponseEntity<Void> selectSeat(@PathVariable Long seatId, @CurrentAccount Long memberId) {
		seatService.selectSeat(seatId, memberId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "좌석 선택 해제")
	@PostMapping("/{seatId}/deselect")
	public ResponseEntity<Void> deselectSeat(@PathVariable Long seatId, @CurrentAccount Long memberId) {
		seatService.deselectSeat(seatId, memberId);
		return ResponseEntity.noContent().build();
	}
}
