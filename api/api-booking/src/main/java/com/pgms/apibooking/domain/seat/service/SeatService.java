package com.pgms.apibooking.domain.seat.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.domain.seat.dto.request.SeatsGetRequest;
import com.pgms.apibooking.domain.seat.dto.response.AreaResponse;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.apibooking.common.exception.BookingException;
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

	public void selectSeat(Long seatId, Long memberId) {
		if (seatLockService.isSeatLocked(seatId)) {
			throw new BookingException(BookingErrorCode.SEAT_BEING_BOOKED);
		}

		EventSeat seat = getSeat(seatId);

		if (seat.isBooked()) {
			throw new BookingException(BookingErrorCode.SEAT_ALREADY_BOOKED);
		}

		seat.updateStatus(EventSeatStatus.BEING_BOOKED);
		seatLockService.lockSeat(seatId, memberId);
	}

	public void deselectSeat(Long seatId, Long memberId) {
		Optional<Long> selectorIdOpt = seatLockService.getSelectorId(seatId);

		if(selectorIdOpt.isEmpty()) {
			updateSeatStatusToAvailable(seatId);
			throw new BookingException(BookingErrorCode.SEAT_SELECTION_EXPIRED);
		}
		
		if (!selectorIdOpt.get().equals(memberId)) {
			throw new BookingException(BookingErrorCode.SEAT_SELECTED_BY_ANOTHER_MEMBER);
		}

		updateSeatStatusToAvailable(seatId);
		seatLockService.unlockSeat(seatId);
	}

	private EventSeat getSeat(Long seatId) {
		return eventSeatRepository.findById(seatId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.SEAT_NOT_FOUND));
	}

	private void updateSeatStatusToAvailable(Long seatId) {
		EventSeat seat = getSeat(seatId);
		seat.updateStatus(EventSeatStatus.AVAILABLE);
	}
}
