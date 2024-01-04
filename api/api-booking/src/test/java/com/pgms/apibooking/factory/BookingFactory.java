package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.member.Member;

public class BookingFactory {

	public static Booking generate(Member member, EventTime time, Integer amount, BookingStatus status) {
		return Booking.builder()
			.id("1704356559328")
			.bookingName("BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL 1")
			.receiptType(ReceiptType.PICK_UP)
			.buyerName("구매자 명")
			.buyerPhoneNumber("010-1234-5678")
			.amount(amount)
			.status(status)
			.member(member)
			.time(time)
			.build();
	}
}
