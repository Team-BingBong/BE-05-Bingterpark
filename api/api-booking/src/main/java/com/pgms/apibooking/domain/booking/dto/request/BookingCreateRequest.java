package com.pgms.apibooking.domain.booking.dto.request;

import java.util.List;
import java.util.Optional;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventTime;
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
	String receiptType,

	//TODO : 에매자명 = 로그인한 사용자 명으로 설정하기
	@NotBlank(message = "[구매자 명] 입력은 필수입니다.")
	String buyerName,

	@NotBlank(message = "[구매자 번호] 입력은 필수입니다.")
	String buyerPhoneNumber,

	@Valid
	Optional<DeliveryAddress> deliveryAddress
) {

	public static Booking toEntity(
		BookingCreateRequest request,
		EventTime time,
		List<EventSeat> seats,
		Member member
	) {
		DeliveryAddress deliveryAddress = request.deliveryAddress.orElse(null);
		return Booking.builder()
			.id(String.valueOf(System.currentTimeMillis()))
			.bookingName(time.getEvent().getTitle() + " " + time.getRound())
			.status(BookingStatus.WAITING_FOR_PAYMENT)
			.receiptType(ReceiptType.fromDescription(request.receiptType))
			.buyerName(request.buyerName)
			.buyerPhoneNumber(request.buyerPhoneNumber)
			.recipientName(deliveryAddress != null ? deliveryAddress.recipientName() : null)
			.recipientPhoneNumber(deliveryAddress != null ? deliveryAddress.recipientPhoneNumber() : null)
			.streetAddress(deliveryAddress != null ? deliveryAddress.streetAddress() : null)
			.detailAddress(deliveryAddress != null ? deliveryAddress.detailAddress() : null)
			.zipCode(deliveryAddress != null ? deliveryAddress.zipCode() : null)
			.amount(seats.stream()
				.map(seat -> seat.getEventSeatArea().getPrice())
				.reduce(0, Integer::sum))
			.member(member)
			.time(time)
			.build();
	}
}
