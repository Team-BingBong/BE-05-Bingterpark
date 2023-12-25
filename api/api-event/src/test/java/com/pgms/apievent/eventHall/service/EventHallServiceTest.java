package com.pgms.apievent.eventHall.service;

import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallSeat;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
class EventHallServiceTest {

    private final EventHallRepository eventHallRepository;

    @Test
    public void 공연장_생성_성공(){
        // given
        List<EventHallSeat> eventHallSeats = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeat("T" + String.valueOf(i)))
                .toList();

        EventHall eventHall = EventHall.builder()
                .name("test")
                .address("test")
                .eventHallSeats(eventHallSeats)
                .build();

        // when
        EventHall savedEventHall = eventHallRepository.save(eventHall);

        // then
        assertThat(savedEventHall.getName(), is("test"));
        assertThat(savedEventHall.getEventHallSeats().size(), is(10L));
    }

}
