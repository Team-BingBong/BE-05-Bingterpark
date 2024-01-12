package com.pgms.apibooking.domain.seat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.domain.seat.dto.request.SeatsGetRequest;
import com.pgms.apibooking.domain.seat.dto.response.AreaResponse;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService { //TODO: 테스트 코드 작성

	private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 420;

	private final EventSeatRepository eventSeatRepository;
	private final SeatLockManager seatLockManager;

	@Transactional(readOnly = true)
	public List<AreaResponse> getSeats(SeatsGetRequest request) {
		return eventSeatRepository.findAllWithAreaByTimeId(request.eventTimeId()).stream()
			.collect(Collectors.groupingBy(es -> es.getEventSeatArea().getId()))
			.values().stream()
			.map(AreaResponse::from)
			.toList();
	}

	public void selectSeat(Long seatId, Long memberId) {
		Long selectorId = seatLockManager.getSelectorId(seatId).orElse(null);

		if (selectorId.equals(memberId)) {
			return;
		}

		if (selectorId != null) {
			throw new BookingException(BookingErrorCode.SEAT_HELD_BY_ANOTHER_MEMBER);
		}

		EventSeat seat = getSeat(seatId);

		if (seat.isBooked()) {
			throw new BookingException(BookingErrorCode.SEAT_ALREADY_BOOKED);
		}

		seat.updateStatus(EventSeatStatus.HOLDING);
		seatLockManager.lockSeat(seatId, memberId, SEAT_LOCK_CACHE_EXPIRE_SECONDS);
	}

	public void deselectSeat(Long seatId, Long memberId) {
		Long selectorId = seatLockManager.getSelectorId(seatId).orElse(null);

		if (selectorId == null) {
			updateSeatStatusToAvailable(seatId);
			return;
		}

		if (!selectorId.equals(memberId)) {
			throw new BookingException(BookingErrorCode.SEAT_HELD_BY_ANOTHER_MEMBER);
		}

		EventSeat seat = getSeat(seatId);

		if (seat.isBooked()) {
			throw new BookingException(BookingErrorCode.SEAT_ALREADY_BOOKED);
		}

		seat.updateStatus(EventSeatStatus.AVAILABLE);
		seatLockManager.unlockSeat(seatId);
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
