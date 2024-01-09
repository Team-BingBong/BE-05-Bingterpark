package com.pgms.apibooking.domain.booking.repository;

import static com.pgms.coredomain.domain.booking.QBooking.*;
import static com.pgms.coredomain.domain.member.QMember.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pgms.apibooking.domain.booking.dto.request.BookingSearchCondition;
import com.pgms.apibooking.domain.booking.dto.request.BookingSortType;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQuerydslRepositoryImpl implements BookingQuerydslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Booking> findAll(BookingSearchCondition condition, Pageable pageable) {
		return queryFactory
			.selectFrom(booking)
			.join(booking.member, member)
			.where(createFilterCondition(condition))
			.orderBy(createSortCondition(condition.getSortType()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public Long count(BookingSearchCondition condition) {
		return queryFactory
			.select(booking.count())
			.join(booking.member, member)
			.from(booking)
			.where(createFilterCondition(condition))
			.fetchOne();
	}

	private BooleanExpression[] createFilterCondition(BookingSearchCondition condition) {
		return new BooleanExpression[] {
			booking.member.id.eq(condition.getMemberId()),
			condition.getId() == null ? null : booking.id.eq(condition.getId()),
			condition.getStatus() == null ? null : booking.status.eq(BookingStatus.fromDescription(condition.getStatus())),
			createdAtGoe(condition.getMaxCreatedAt() == null ? null : condition.getMaxCreatedAt()),
			createdAtLoe(condition.getMinCreatedAt() == null ? null : condition.getMinCreatedAt())
		};
	}

	private BooleanExpression createdAtGoe(LocalDate date) {
		return date == null ? null : booking.createdAt.goe(date.atStartOfDay());
	}

	private BooleanExpression createdAtLoe(LocalDate date) {
		return date == null ? null : booking.createdAt.loe(date.atStartOfDay());
	}

	private OrderSpecifier<?> createSortCondition(BookingSortType sortType) {
		if (sortType == null) {
			return booking.createdAt.desc();
		}

		return switch (sortType) {
			case LATEST -> booking.createdAt.desc();
			case OLDEST -> booking.createdAt.asc();
		};
	}
}