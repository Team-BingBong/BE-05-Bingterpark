package com.pgms.apievent.event.repository;

import static com.pgms.coredomain.domain.booking.QBooking.*;
import static com.pgms.coredomain.domain.event.QEvent.*;
import static com.pgms.coredomain.domain.event.QEventReview.*;
import static com.pgms.coredomain.domain.event.QEventTime.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pgms.apievent.event.dto.request.EventPageRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.coredomain.domain.event.DateRangeType;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coreinfraes.dto.EventKeywordSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<EventResponse> getEventsPageByGenreSortedByRanking(EventPageRequest eventPageRequest) {

		GenreType genre = GenreType.of(eventPageRequest.getGenreType());
		Pageable pageable = eventPageRequest.getPageable();
		int offset = (eventPageRequest.getPage() - 1) * eventPageRequest.getSize();

		DateRangeType dateRangeType = DateRangeType.of(eventPageRequest.getDateRange());
		LocalDateTime cmpDate = LocalDateTime.now().minusDays(dateRangeType.getDateRange())
			.with(LocalTime.MIN);

		List<EventResponse> content = jpaQueryFactory.selectFrom(event)
			.leftJoin(eventTime)
			.on(eventTime.event.eq(event))
			.leftJoin(booking)
			.on(booking.time.eq(eventTime))
			.where(event.genreType.eq(genre), booking.createdAt.coalesce(cmpDate).goe(cmpDate))
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

	@Override
	public Page<EventResponse> getEventsByKeyword(EventKeywordSearchDto eventKeywordSearchDto) {
		Pageable pageable = eventKeywordSearchDto.pageable();
		int offset = pageable.getPageNumber() * pageable.getPageSize();

		List<EventResponse> content = jpaQueryFactory.selectFrom(event)
				.where(
					containsKeyword(eventKeywordSearchDto.keyword()),
					isGenreType(eventKeywordSearchDto.genreType())
				)
				.offset(offset)
				.limit(pageable.getPageSize())
				.fetch()
				.stream()
				.map(EventResponse::of)
				.toList();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(event.count())
				.from(event)
				.where(
					containsKeyword(eventKeywordSearchDto.keyword()),
					isGenreType(eventKeywordSearchDto.genreType())
				);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanBuilder containsKeyword(String keyword){
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if(keyword == null) {
			return null;
		}
		booleanBuilder.or(event.title.contains(keyword));
		booleanBuilder.or(event.description.contains(keyword));

		return booleanBuilder;
	}

	private BooleanBuilder isGenreType(List<String> genres){
		if(genres == null || genres.isEmpty()){
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();
		genres.stream()
				.forEach(genre -> {
					GenreType genreType = GenreType.of(genre);
					booleanBuilder.or(event.genreType.eq(genreType));
				});
		return booleanBuilder;
	}
}
