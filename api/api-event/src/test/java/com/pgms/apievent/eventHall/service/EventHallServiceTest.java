package com.pgms.apievent.eventHall.service;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallSeatCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallUpdateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallSeat;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Transactional
class EventHallServiceTest {

    @Autowired
    private EventHallRepository eventHallRepository;
    @Autowired
    private EventHallService eventHallService;

    @BeforeEach
    void clean(){
        eventHallRepository.deleteAll();
    }

    @Test
    public void 공연장_생성_성공(){
        // given
        List<EventHallSeatCreateRequest> eventHallSeatCreateRequests = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeatCreateRequest("T" + String.valueOf(i)))
                .toList();

        EventHallCreateRequest eventHallCreateRequest = new EventHallCreateRequest("test", "test", eventHallSeatCreateRequests);

        // when
        EventHallResponse eventHallResponse = eventHallService.createEventHall(eventHallCreateRequest);

        // then
        assertThat(eventHallResponse.name(), is("test"));
        assertThat(eventHallResponse.eventHallSeatResponses().size(), is(10));
    }

    @Test
    public void 공연장_삭제_성공(){
        // given
        List<EventHallSeat> eventHallSeats = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeat("T" + String.valueOf(i)))
                .toList();

        EventHall eventHall = EventHall.builder()
                .name("test")
                .address("test")
                .eventHallSeats(eventHallSeats)
                .build();

        EventHall savedEventHall = eventHallRepository.save(eventHall);

        // when
        eventHallRepository.delete(savedEventHall);

        // then
        assertThat(eventHallRepository.count(), is(0L));
    }

    @Test
    public void 공연장_수정_성공(){
        // given
        List<EventHallSeat> eventHallSeats = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeat("T" + String.valueOf(i)))
                .toList();

        EventHall eventHall = EventHall.builder()
                .name("test")
                .address("test")
                .eventHallSeats(eventHallSeats)
                .build();

        EventHall savedEventHall = eventHallRepository.save(eventHall);

        List<EventHallSeatCreateRequest> eventHallSeatCreateRequests = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeatCreateRequest("T" + String.valueOf(i)))
                .toList();

        EventHallUpdateRequest eventHallUpdateRequest = new EventHallUpdateRequest("update", "test", eventHallSeatCreateRequests);

        // when
        EventHallResponse eventHallResponse = eventHallService.updateEventHall(savedEventHall.getId(), eventHallUpdateRequest);

        // then
        assertThat(eventHallResponse.name(), is("update"));
        assertThat(savedEventHall.getEventHallSeats().size(), is(10));
    }

    @Test
    public void 공연장_목록_조회_성공(){
        // given

        List<EventHallSeat> eventHallSeats = IntStream.range(0, 10)
                .mapToObj(i -> new EventHallSeat("T" + String.valueOf(i)))
                .toList();

        List<EventHall> eventHalls = IntStream.range(0, 10)
                .mapToObj(i -> {
                    return EventHall.builder()
                            .name("test")
                            .address("test")
                            .eventHallSeats(eventHallSeats)
                            .build();
                })
                .toList();

        // when
        List<EventHall> savedEventHalls = eventHallRepository.saveAll(eventHalls);

        // then
        assertThat(savedEventHalls.size(), is(10));
    }
}
