package com.pgms.apievent.eventSeat.service;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.coredomain.domain.event.*;
import com.pgms.coredomain.domain.event.repository.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;

@Transactional
@SpringBootTest
class EventSeatServiceTest {

    @Autowired
    private EventSeatService eventSeatService;
    @Autowired
    private EventSeatRepository eventSeatRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTimeRepository eventTimeRepository;
    @Autowired
    private EventHallRepository eventHallRepository;
    @Autowired
    private EventSeatAreaRepository eventSeatAreaRepository;

    private EventHall eventHall;
    private Event event;

    @BeforeEach
    void setUp() {
        eventHall = EventHallFactory.createEventHall();
        eventHallRepository.save(eventHall);
        event = eventRepository.save(EventFactory.createEvent(eventHall));
    }

    @Test
    void 공연_좌석_생성_성공() {
        // given
        List<EventTime> eventTimes = IntStream.range(1, 5)
                .mapToObj(i ->
                        new EventTime(i, LocalDateTime.now(), LocalDateTime.now(), event))
                .toList();
        eventTimeRepository.saveAll(eventTimes);

        EventSeatArea eventSeatArea = new EventSeatArea(SeatAreaType.R, 1000, event);
        eventSeatAreaRepository.save(eventSeatArea);

        List<EventSeatsCreateRequest> eventSeatsCreateRequests = IntStream.range(0, 20)
                .mapToObj(i ->
                        new EventSeatsCreateRequest("", EventSeatStatus.AVAILABLE, eventSeatArea))
                .toList();

        // when
        eventSeatService.createEventSeats(event.getId(), eventSeatsCreateRequests);

        // then
        MatcherAssert.assertThat(eventSeatRepository.count(), is(80L));
    }

    @Test
    void 여석_갯수_조회_성공() {
        // given


        // when

        // then

    }

}
