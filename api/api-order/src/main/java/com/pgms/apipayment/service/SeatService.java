package com.pgms.apipayment.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pgms.apipayment.dto.request.SeatsGetRequest;
import com.pgms.apipayment.dto.response.AreaResponse;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.repository.event.EventSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

	private final EventSeatRepository eventSeatRepository;

	//TODO: 테스트 코드 작성
	public List<AreaResponse> getSeats(SeatsGetRequest request) {
		List<EventSeat> seats = eventSeatRepository.findAllWithAreaByEventTimeId(request.eventTimeId());

		Map<Long, List<EventSeat>> seatsByArea = seats.stream()
			.collect(java.util.stream.Collectors.groupingBy(es -> es.getEventSeatArea().getId()));

		return seatsByArea.values().stream()
			.map(AreaResponse::from)
			.toList();
	}
}
