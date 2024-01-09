package com.pgms.apievent.event.repository;

import com.pgms.apievent.event.dto.request.EventPageRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.coredomain.domain.event.GenreType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.pgms.coredomain.domain.booking.QBooking.booking;
import static com.pgms.coredomain.domain.event.QEvent.event;
import static com.pgms.coredomain.domain.event.QEventReview.eventReview;
import static com.pgms.coredomain.domain.event.QEventTime.eventTime;

@Repository
@RequiredArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<EventResponse> getEventsPageByGenreSortedByRanking(EventPageRequest eventPageRequest) {

		GenreType genre = GenreType.of(eventPageRequest.getGenreType());
		Pageable pageable = eventPageRequest.getPageable();
		int offset = (eventPageRequest.getPage() - 1) * eventPageRequest.getSize();

		LocalDateTime cmpDate = LocalDateTime.now().minusDays(eventPageRequest.getDateOffset())
			.with(LocalTime.MIN);

		List<EventResponse> content = jpaQueryFactory.selectFrom(event)
			.leftJoin(eventTime)
			.on(eventTime.event.eq(event))
			.leftJoin(booking)
			.on(booking.time.eq(eventTime))
			.where(event.genreType.eq(genre), booking.createdAt.goe(cmpDate))
			.groupBy(event.id)
			.orderBy(booking.count().desc())
			.offset(offset)
			.limit(pageable.getPageSize())
			.fetch()
			.stream()
			.map(EventResponse::of)
			.toList();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(event.count())
			.from(event)
			.leftJoin(eventTime).fetchJoin()
			.on(eventTime.event.eq(event))
			.leftJoin(booking).fetchJoin()
			.on(eventTime.id.eq(booking.time.id))
			.where(event.genreType.eq(genre), booking.createdAt.goe(cmpDate))
			.groupBy(event.id);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventResponse> getEventsPageByGenreSortedByReview(EventPageRequest eventPageRequest) {
		GenreType genre = GenreType.of(eventPageRequest.getGenreType());
		Pageable pageable = eventPageRequest.getPageable();
		int offset = (eventPageRequest.getPage() - 1) * eventPageRequest.getSize();

		List<EventResponse> content = jpaQueryFactory.selectFrom(event)
			.leftJoin(eventReview)
			.on(eventReview.event.eq(event))
			.where(event.genreType.eq(genre))
			.groupBy(event.id)
			.orderBy(eventReview.count().desc())
			.offset(offset)
			.limit(pageable.getPageSize())
			.fetch()
			.stream()
			.map(EventResponse::of)
			.toList();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(event.count())
			.from(event)
			.leftJoin(eventReview)
			.on(eventReview.event.eq(event))
			.where(event.genreType.eq(genre))
			.groupBy(event.id);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<EventResponse> getEventsPageByGenreSortedByBookingEndedAt(EventPageRequest eventPageRequest) {
		GenreType genre = GenreType.of(eventPageRequest.getGenreType());
		Pageable pageable = eventPageRequest.getPageable();
		LocalDateTime cmpDate = LocalDateTime.now();
		int offset = (eventPageRequest.getPage() - 1) * eventPageRequest.getSize();

		List<EventResponse> content = jpaQueryFactory.selectFrom(event)
			.where(
				event.bookingEndedAt.gt(cmpDate),
				event.genreType.eq(genre)
			)
			.orderBy(event.bookingEndedAt.asc())
			.offset(offset)
			.limit(pageable.getPageSize())
			.fetch()
			.stream()
			.map(EventResponse::of)
			.toList();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(event.count())
			.from(event)
			.where(
				event.bookingEndedAt.gt(cmpDate),
				event.genreType.eq(genre)
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}
}
