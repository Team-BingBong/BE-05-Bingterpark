package com.pgms.apibooking.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookingQueueEnterRequest(@NotNull(message = "[공연] 선택은 필수입니다.") Long eventId) {
}
