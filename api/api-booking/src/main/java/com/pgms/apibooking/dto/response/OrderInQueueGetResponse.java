package com.pgms.apibooking.dto.response;

public record OrderInQueueGetResponse(Long myOrder, Boolean isMyTurn) {

	public static OrderInQueueGetResponse of(Long myOrder, Boolean isMyTurn) {
		return new OrderInQueueGetResponse(myOrder, isMyTurn);
	}
}
