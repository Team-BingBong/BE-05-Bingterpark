package com.pgms.apibooking.dto.response;

import java.util.List;

public record PaymentCancelResponse(
	String paymentKey,
	String orderId,
	String orderName,	// 토스페이먼츠에서 제공해주는 값이라 booing으로 변경x
	String method,
	String totalAmount,
	String status,
	String requestedAt,
	String approvedAt,
	PaymentSuccessCardResponse card,
	List<PaymentCancelDetailResponse> cancels
) {
}
