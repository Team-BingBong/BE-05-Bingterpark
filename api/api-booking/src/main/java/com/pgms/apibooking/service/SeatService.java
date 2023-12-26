package com.pgms.apibooking.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.SeatsGetRequest;
import com.pgms.apibooking.dto.response.AreaResponse;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.repository.event.EventSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {

	private final EventSeatRepository eventSeatRepository;

	//TODO: 테스트 코드 작성
	@Transactional(readOnly = true)
	public List<AreaResponse> getSeats(SeatsGetRequest request) {
		List<EventSeat> seats = eventSeatRepository.findAllWithAreaByEventTimeId(request.eventTimeId());

		Map<Long, List<EventSeat>> seatsByArea = seats.stream()
			.collect(Collectors.groupingBy(es -> es.getEventSeatArea().getId()));

		return seatsByArea.values().stream()
			.map(AreaResponse::from)
			.toList();
	}
}
