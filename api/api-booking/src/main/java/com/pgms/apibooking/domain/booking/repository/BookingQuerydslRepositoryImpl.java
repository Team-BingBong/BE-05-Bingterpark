package com.pgms.apibooking.domain.booking.repository;

import static com.pgms.coredomain.domain.booking.QBooking.*;
import static com.pgms.coredomain.domain.booking.QPayment.*;
import static com.pgms.coredomain.domain.event.QEventTime.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pgms.apibooking.domain.booking.dto.request.BookingSearchCondition;
import com.pgms.apibooking.domain.booking.dto.request.BookingSortType;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.PaymentStatus;
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
			.join(booking.payment, payment).fetchJoin()
			.join(booking.time, eventTime).fetchJoin()
			.leftJoin(booking.cancel).fetchJoin()
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
			.join(booking.payment, payment).fetchJoin()
			.from(booking)
			.where(createFilterCondition(condition))
			.fetchOne();
	}

	private BooleanExpression[] createFilterCondition(BookingSearchCondition condition) {
		return new BooleanExpression[] {
			booking.member.id.eq(condition.getMemberId()),
			booking.payment.status.ne(PaymentStatus.READY),
			condition.getId() == null ? null : booking.id.eq(condition.getId()),
			condition.getStatus() == null ? null : booking.status.eq(BookingStatus.fromDescription(condition.getStatus())),
			createdAtGoe(condition.getMinCreatedAt() == null ? null : condition.getMinCreatedAt()),
			createdAtLoe(condition.getMaxCreatedAt() == null ? null : condition.getMaxCreatedAt()),
		};
	}

	private BooleanExpression createdAtGoe(LocalDate date) {
		return date == null ? null : booking.createdAt.goe(date.atStartOfDay());
	}

	private BooleanExpression createdAtLoe(LocalDate date) {
		return date == null ? null : booking.createdAt.loe(date.atTime(23, 59, 59));
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
