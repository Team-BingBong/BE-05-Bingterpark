package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.apibooking.domain.booking.dto.request.DeliveryAddress;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCardResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentVirtualResponse;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.ReceiptType;

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
	String eventTimeStartedAt,
	String eventTimeEndedAt,
	String receiptType,
	DeliveryAddress deliveryAddress,
	String createdAt,
	String paymentMethod,
	PaymentCardResponse paymentCard,
	PaymentVirtualResponse paymentVirtual,
	String paymentApprovedAt
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
			booking.getTime().getStartedAt().toString(),
			booking.getTime().getEndedAt().toString(),
			booking.getReceiptType().getDescription(),
			booking.getReceiptType() == ReceiptType.PICK_UP ? null :
				DeliveryAddress.of(
					booking.getRecipientName(),
					booking.getRecipientPhoneNumber(),
					booking.getStreetAddress(),
					booking.getDetailAddress(),
					booking.getZipCode()
				),
			booking.getCreatedAt().toString(),
			booking.getPayment().getMethod().getDescription(),
			booking.getPayment().getMethod() == PaymentMethod.VIRTUAL_ACCOUNT ? null :
				PaymentCardResponse.from(booking.getPayment()),
			booking.getPayment().getMethod() == PaymentMethod.CARD ? null :
				PaymentVirtualResponse.from(booking.getPayment()),
			booking.getPayment().getApprovedAt().toString()
		);
	}
}
