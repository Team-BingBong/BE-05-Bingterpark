package com.pgms.apibooking.domain.booking.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingSearchCondition {

	private Long memberId;
	private String id;
	private String status;
	private LocalDate minCreatedAt;
	private LocalDate maxCreatedAt;
	private BookingSortType sortType;

	public void updateMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
