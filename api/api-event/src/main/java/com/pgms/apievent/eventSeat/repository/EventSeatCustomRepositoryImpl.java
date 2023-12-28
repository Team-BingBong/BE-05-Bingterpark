package com.pgms.apievent.eventSeat.repository;

import com.pgms.apievent.eventSeat.dto.LeftEventSeatNumDto;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.QEventSeat;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventSeatCustomRepositoryImpl implements EventSeatCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    @Override
    public void updateEventSeatsSeatArea(Long[] ids, EventSeatArea eventSeatArea) {
        QEventSeat qEventSeat = QEventSeat.eventSeat;

        jpaQueryFactory
                .update(qEventSeat)
                .set(qEventSeat.eventSeatArea, eventSeatArea)
                .where(qEventSeat.id.in(ids))
                .execute();

        // 벌크 연산 시 DB엔 반영, 영속성 컨텍스트에는 반영되지 않음
        em.clear();
    }

    @Override
    public void updateEventSeatsStatus(Long[] ids, EventSeatStatus eventSeatStatus) {
        QEventSeat qEventSeat = QEventSeat.eventSeat;

        jpaQueryFactory
                .update(qEventSeat)
                .set(qEventSeat.status, eventSeatStatus)
                .where(qEventSeat.id.in(ids))
                .execute();

        // 벌크 연산 시 DB엔 반영, 영속성 컨텍스트에는 반영되지 않음
        em.clear();
    }

    @Override
    public void deleteEventSeats(Long[] ids) {
        QEventSeat qEventSeat = QEventSeat.eventSeat;

        jpaQueryFactory
                .delete(qEventSeat)
                .where(qEventSeat.id.in(ids))
                .execute();

        // 벌크 연산 시 DB엔 반영, 영속성 컨텍스트에는 반영되지 않음
        em.clear();
    }

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
