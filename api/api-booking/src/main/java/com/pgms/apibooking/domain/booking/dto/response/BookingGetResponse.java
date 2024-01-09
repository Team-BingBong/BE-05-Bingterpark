package com.pgms.apibooking.domain.booking.dto.response;

import java.time.LocalDateTime;

import com.pgms.apibooking.domain.booking.dto.request.DeliveryAddress;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCardResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentVirtualResponse;
import com.pgms.coredomain.domain.booking.Booking;

public record BookingGetResponse(
	String id,
	Integer amount,
	String status,
	String buyerName,
	String buyerPhoneNumber,
	Long eventId,
	String eventThumbnail,
	String eventName,
	Integer eventTime,
	LocalDateTime eventTimeStartedAt,
	LocalDateTime eventTimeEndedAt,
	String receiptType,
	DeliveryAddress deliveryAddress,
	LocalDateTime createdAt,
	String paymentMethod,
	PaymentCardResponse paymentCard,
	PaymentVirtualResponse paymentVirtual,
	LocalDateTime paymentApprovedAt
) {

	public static BookingGetResponse from(Booking booking) {
		return new BookingGetResponse(
			booking.getId(),
			booking.getAmount(),
			booking.getStatus().name(),
			booking.getBuyerName(),
			booking.getBuyerPhoneNumber(),
			booking.getTime().getEvent().getId(),
			booking.getTime().getEvent().getThumbnail(),
			booking.getTime().getEvent().getTitle(),
			booking.getTime().getRound(),
			booking.getTime().getStartedAt(),
			booking.getTime().getEndedAt(),
			booking.getReceiptType().getDescription(),
			DeliveryAddress.of(
				booking.getRecipientName(),
				booking.getRecipientPhoneNumber(),
				booking.getStreetAddress(),
				booking.getDetailAddress(),
				booking.getZipCode()
			),
			booking.getCreatedAt(),
			booking.getPayment().getMethod().getDescription(),
			PaymentCardResponse.from(booking.getPayment()),
			PaymentVirtualResponse.from(booking.getPayment()),
			booking.getPayment().getApprovedAt()
		);
	}
}
