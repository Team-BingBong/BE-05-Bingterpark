package com.pgms.apievent.eventSeat.repository;

import com.pgms.apievent.eventSeat.dto.LeftEventSeatNumDto;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.QEventSeat;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventSeatCustomRepositoryImpl implements EventSeatCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LeftEventSeatNumDto> getLeftEventSeatNumberByEventTime(EventTime eventTime) {

        QEventSeat qEventSeat = QEventSeat.eventSeat;
        return jpaQueryFactory
                .select(
                        Projections.bean(
                                LeftEventSeatNumDto.class,
                                qEventSeat.eventSeatArea,
                                qEventSeat.count()
                        )
                )
                .from(qEventSeat)
                .where(qEventSeat.eventTime.eq(eventTime),
                        qEventSeat.status.eq(EventSeatStatus.AVAILABLE))
                .groupBy(qEventSeat.eventSeatArea)
                .fetch();
    }
}
