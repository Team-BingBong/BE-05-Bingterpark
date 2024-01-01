package com.pgms.apievent.event.dto.request;

import com.pgms.coredomain.domain.event.GenreType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record EventUpdateRequest(

	@Nullable
	@Size(min = 1, max = 100, message = "공연 제목은 최소 1자 이상 최대 100자 이하 입니다.")
	String title,

	@Nullable
	String description,

	@Positive(message = "공연 러닝 타임은 0보다 큰 값 이어야 합니다.")
	int runningTime,

	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDateTime startDate,

	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDateTime endDate,

	@Nullable
	String viewRating,

	@Nullable
	GenreType genreType,

	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDateTime bookingStartedAt,

	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDateTime bookingEndedAt,

	@Nullable
	Long eventHallId
) {
}
