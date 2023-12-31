package com.pgms.apibooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BookingCancelRequest(@NotBlank String cancelReason) {
}
