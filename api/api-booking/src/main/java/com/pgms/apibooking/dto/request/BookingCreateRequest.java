package com.pgms.apibooking.dto.request;

import java.util.List;
import java.util.Optional;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.Ticket;
import com.pgms.coredomain.domain.member.Member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookingCreateRequest(

	@NotNull
	Long timeId,

	@NotNull(message = "[공연 좌석] 선택은 필수입니다.")
	@NotEmpty(message = "[공연 좌석] 선택은 필수입니다.")
	List<Long> seatIds,

	@NotNull(message = "[수령 방법] 선택은 필수입니다.")
	ReceiptType receiptType,

	@NotBlank(message = "[구매자 명] 입력은 필수입니다.")
	String buyerName,

	@NotBlank(message = "[구매자 번호] 입력은 필수입니다.")
	String buyerPhoneNumber,

	@Valid
	Optional<DeliveryAddress> deliveryAddress,

	@NotNull(message = "[결제 수단] 선택은 필수입니다.")
	PaymentMethod method
) {

	public Booking toEntity(EventTime time, List<EventSeat> seats, Member member) {
		Booking booking = Booking.builder()
			.id(String.valueOf(System.currentTimeMillis()))
			.bookingName(time.getEvent().getTitle() + " " + time.getRound())
			.status(BookingStatus.WAITING_FOR_DEPOSIT)
			.receiptType(receiptType())
			.buyerName(buyerName())
			.buyerPhoneNumber(buyerPhoneNumber())
			.recipientName(deliveryAddress().isPresent()
				? deliveryAddress().get().recipientName() : null)
			.recipientPhoneNumber(deliveryAddress().isPresent()
				? deliveryAddress().get().recipientPhoneNumber() : null)
			.streetAddress(deliveryAddress().isPresent()
				? deliveryAddress().get().streetAddress() : null)
			.detailAddress(deliveryAddress().isPresent()
				? deliveryAddress().get().detailAddress() : null)
			.zipCode(deliveryAddress().isPresent()
				? deliveryAddress().get().zipCode() : null)
			.amount(seats.stream()
				.map(seat -> seat.getEventSeatArea().getPrice())
				.reduce(0, Integer::sum))
			.member(member)
			.build();

		seats.forEach(seat -> booking.addTicket(
			Ticket.builder()
				.seat(seat)
				.build())
		);

		booking.updatePayment(
			Payment.builder()
				.method(method())
				.amount(booking.getAmount())
				.status(PaymentStatus.WAITING_FOR_DEPOSIT)
				.build()
		);

		return booking;
	}
}
