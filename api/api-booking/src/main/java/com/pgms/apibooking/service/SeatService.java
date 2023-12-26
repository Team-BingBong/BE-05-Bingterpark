package com.pgms.apibooking.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.SeatSelectRequest;
import com.pgms.apibooking.dto.request.SeatDeselectRequest;
import com.pgms.apibooking.dto.request.SeatsGetRequest;
import com.pgms.apibooking.dto.response.AreaResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
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

	public void selectSeat(SeatSelectRequest request) {
		/*
		 * TODO: (seatId, memberId) 캐시 조회
		 * 만약 캐시에서 조회되지 않는데 상태값이 IN_PROGRESS인 경우
		 * 캐시에 멤버아이디 다시 세팅
		 */

		EventSeat seat = eventSeatRepository.findById(request.seatId())
			.orElseThrow(() -> new BookingException(BookingErrorCode.SEAT_NOT_FOUND));

		if (!seat.isAvailable()) {
			throw new BookingException(BookingErrorCode.SEAT_BEING_BOOKED);
		}

		//TODO: (seatId, memberId) 캐시 저장

		seat.updateStatus(EventSeatStatus.BEING_BOOKED);
	}

	public void deselectSeat(SeatDeselectRequest request) {
		//TODO: (seatId, memberId) 캐시 조회

		EventSeat seat = eventSeatRepository.findById(request.seatId())
			.orElseThrow(() -> new BookingException(BookingErrorCode.SEAT_NOT_FOUND));

		//TODO: (seatId, memberId) 캐시 삭제

		seat.updateStatus(EventSeatStatus.AVAILABLE);
	}
}
