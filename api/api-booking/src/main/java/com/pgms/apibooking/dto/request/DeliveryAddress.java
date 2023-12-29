package com.pgms.apibooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeliveryAddress(

	@NotBlank(message = "[수령자 이름] 입력은 필수입니다.")
	String recipientName,

	@NotBlank(message = "[수령자 번호] 입력은 필수입니다.")
	String recipientPhoneNumber,

	@NotBlank(message = "[도로명 주소] 입력은 필수입니다.")
	String streetAddress,

	@NotBlank(message = "[상세 주소] 입력은 필수입니다.")
	String detailAddress,

	@NotBlank(message = "[우편 번호] 입력은 필수입니다.")
	String zipCode
) {
}
