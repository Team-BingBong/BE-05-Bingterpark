package com.pgms.apibooking.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.SeatsGetRequest;
import com.pgms.apibooking.dto.response.AreaResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService { //TODO: 테스트 코드 작성

	private final EventSeatRepository eventSeatRepository;
	private final SeatLockService seatLockService;

	@Transactional(readOnly = true)
	public List<AreaResponse> getSeats(SeatsGetRequest request) {
		List<EventSeat> seats = eventSeatRepository.findAllWithAreaByTimeId(request.eventTimeId());

		Map<Long, List<EventSeat>> seatsByArea = seats.stream()
			.collect(Collectors.groupingBy(es -> es.getEventSeatArea().getId()));

		return seatsByArea.values().stream()
			.map(AreaResponse::from)
			.toList();
	}

	public void selectSeat(Long seatId) {
		Long memberId = 0L; //TODO: 인증된 memberId 지정

		if (seatLockService.isSeatLocked(seatId)) {
			throw new BookingException(BookingErrorCode.SEAT_BEING_BOOKED);
		}

		EventSeat seat = getSeat(seatId);

		if (seat.isBooked()) {
			throw new BookingException(BookingErrorCode.SEAT_BOOKED);
		}

		seat.updateStatus(EventSeatStatus.BEING_BOOKED);
		seatLockService.lockSeat(seatId, memberId);
	}

	public void deselectSeat(Long seatId) {
		Long memberId = 0L; //TODO: 인증된 memberId 지정

		//TODO: lock 걸어둔 멤버에게 요청이 들어왔는지 검증 후, 아래 로직을 수행한다

		EventSeat seat = getSeat(seatId);

		seat.updateStatus(EventSeatStatus.AVAILABLE);
		seatLockService.unlockSeat(seatId);
	}

	private EventSeat getSeat(Long seatId) {
		return eventSeatRepository.findById(seatId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.SEAT_NOT_FOUND));
	}
}
