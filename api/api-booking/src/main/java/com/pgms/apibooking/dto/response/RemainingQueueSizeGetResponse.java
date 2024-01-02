package com.pgms.apibooking.dto.response;

public record RemainingQueueSizeGetResponse(Long remainingQueueSize, Boolean isMyTurn) {

	public static RemainingQueueSizeGetResponse of(Long remainingQueueSize, Boolean isMyTurn) {
		return new RemainingQueueSizeGetResponse(remainingQueueSize, isMyTurn);
	}
}
