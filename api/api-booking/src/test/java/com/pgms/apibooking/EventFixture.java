package com.pgms.apibooking;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coredomain.domain.event.SeatAreaType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventFixture {

	private final EventHall eventHall;
	private final Event event;
	private final EventTime eventTime;
	private final EventSeatArea eventSeatArea;
	private final EventSeat eventSeat;

	public static EventFixture generate() {
		LocalDateTime now = LocalDateTime.now();

		EventHall eventHall = EventHall.builder()
			.name("고척스카이돔")
			.address("서울 구로구 경인로 430")
			.build();

		Event event = Event.builder()
			.title("")
			.description("")
			.runningTime(120)
			.startedAt(now.plusDays(2))
			.endedAt(now.plusDays(2).plusMinutes(120))
			.viewRating("15세 이상 관람가")
			.genreType(GenreType.CONCERT)
			.thumbnail("https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif")
			.bookingStartedAt(LocalDateTime.now())
			.bookingEndedAt(LocalDateTime.now().plusDays(1))
			.eventHall(eventHall)
			.build();

		EventTime time = EventTime.builder()
			.round(1)
			.startedAt(now.plusDays(2))
			.endedAt(now.plusDays(2).plusMinutes(120))
			.event(event)
			.build();

		EventSeatArea area = EventSeatArea.builder()
			.seatAreaType(SeatAreaType.S)
			.price(10000)
			.event(event)
			.build();

		EventSeat seat = EventSeat.builder()
			.name("S1")
			.status(EventSeatStatus.AVAILABLE)
			.eventTime(time)
			.eventSeatArea(area)
			.build();

		return new EventFixture(
			eventHall,
			event,
			time,
			area,
			seat
		);
	}
}
