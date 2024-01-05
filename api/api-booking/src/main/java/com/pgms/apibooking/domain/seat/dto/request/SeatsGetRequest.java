package com.pgms.apibooking.domain.seat.dto.request;

import jakarta.validation.constraints.NotNull;

public record SeatsGetRequest(@NotNull(message = "[공연 회차] 선택은 필수입니다.") Long eventTimeId) {
}
