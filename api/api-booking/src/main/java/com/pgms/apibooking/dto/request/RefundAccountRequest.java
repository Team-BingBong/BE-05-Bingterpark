package com.pgms.apibooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefundAccountRequest(

	@NotBlank(message = "[은행] 선택은 필수입니다.")
	String bank,

	@NotBlank(message = "[환불 계좌] 입력은 필수입니다.")
	String accountNumber,

	@NotBlank(message = "[예금주] 입력은 필수입니다.")
	String holderName
) {
}
