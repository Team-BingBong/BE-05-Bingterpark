package com.pgms.apibooking.dto.request;

public record ConfirmVirtualIncomeRequest (
	String createdAt,
	String status,
	String orderId
){
}
