package com.pgms.apibooking.domain.payment.dto.request;

public record ConfirmVirtualIncomeRequest (
	String createdAt,
	String status,
	String orderId
){
}
