package com.pgms.apibooking.domain.bookingqueue.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookingQueueExitRequest(@NotNull(message = "[공연] 선택은 필수입니다.") Long eventId) {
}
