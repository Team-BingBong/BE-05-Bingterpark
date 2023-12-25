package com.pgms.apipayment.dto.request;

import jakarta.validation.constraints.NotNull;

public record SeatSelectRequest(@NotNull(message = "[공연 좌석] 선택은 필수입니다.") Long seatId) {
}
