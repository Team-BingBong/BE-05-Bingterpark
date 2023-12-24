package com.pgms.apipayment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apipayment.dto.request.SeatsGetRequest;
import com.pgms.apipayment.dto.response.AreaResponse;
import com.pgms.apipayment.service.SeatService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

	private final SeatService seatService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AreaResponse>>> getSeats(@ModelAttribute @Valid SeatsGetRequest request) {
		ApiResponse<List<AreaResponse>> response = ApiResponse.ok(seatService.getSeats(request));
		return ResponseEntity.ok().body(response);
	}
}
