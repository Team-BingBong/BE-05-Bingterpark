package com.pgms.apibooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.EventFixture;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

@SpringBootTest
@Transactional
class BookingServiceTest {

	@Autowired
	private EventHallRepository eventHallRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventSeatAreaRepository eventSeatAreaRepository;

	@Autowired
	private EventSeatRepository eventSeatRepository;

	@BeforeEach
	void setUp() {
		EventFixture fixture = EventFixture.generate();
		eventHallRepository.save(fixture.getEventHall());
		eventRepository.save(fixture.getEvent());
		eventTimeRepository.save(fixture.getEventTime());
		eventSeatAreaRepository.save(fixture.getEventSeatArea());
		eventSeatRepository.save(fixture.getEventSeat());
	}
}
