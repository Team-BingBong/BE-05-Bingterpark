package com.pgms.apievent.eventtime.repository;

import static com.pgms.coredomain.domain.event.QEvent.*;
import static com.pgms.coredomain.domain.event.QEventTime.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventTimeCustomRepositoryImpl implements EventTimeCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Boolean isAlreadyExistEventPlayTime(LocalDateTime startedAt, LocalDateTime endedAt, Long eventId) {
		BooleanExpression query = event.id.eq(eventId)
			.and(eventTime.startedAt.lt(endedAt))
			.and(eventTime.endedAt.gt(startedAt));

		return jpaQueryFactory
			.selectFrom(eventTime)
			.join(event).on(eventTime.event.id.eq(event.id))
			.where(query)
			.fetch().isEmpty();
	}
}
