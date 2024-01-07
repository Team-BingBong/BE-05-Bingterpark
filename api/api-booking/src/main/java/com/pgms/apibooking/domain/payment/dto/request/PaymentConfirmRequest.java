package com.pgms.apibooking.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentConfirmRequest(

	@NotBlank(message = "[Payment Key]는 필수 입력값 입니다.")
	String paymentKey,

	@NotNull(message = "[예매 ID]는 필수 입력값 입니다.")
	String orderId, // 토스페이먼츠 요청을 위해 bookingId로 하면 안됨

	@NotNull(message = "[결제 가격]은 필수 입력값 입니다.")
	@Positive(message = "[결제 가격]은 0보다 큰 값이어야 합니다.")
	int amount
) {
}
