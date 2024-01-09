package com.pgms.apievent.event.dto.request;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EventCreateRequest(

	@NotBlank(message = "공연 제목은 필수 입력값 입니다.")
	@Size(min = 1, max = 100, message = "공연 제목은 최소 1자 이상 최대 100자 이하 입니다.")
	String title,

	@NotBlank(message = "공연 설명은 필수 입력값 입니다.")
	String description,

	@Positive(message = "공연 러닝 타임은 0보다 큰 값 이어야 합니다.")
	int runningTime,

	LocalDateTime startedAt,

	LocalDateTime endedAt,

	@NotBlank(message = "관람 등급은 필수 입력값 입니다.")
	String viewRating,

	@NotNull(message = "공연 장르 타입은 필수 입력값 입니다.")
	GenreType genreType,

	@NotNull(message = "이벤트 홀 ID는 필수 입력값 입니다.")
	Long eventHallId) {
	
	public Event toEntity(EventHall eventHall) {
		return Event.builder()
			.title(title)
			.description(description)
			.runningTime(runningTime)
			.startedAt(startedAt)
			.endedAt(endedAt)
			.viewRating(viewRating)
			.genreType(genreType)
			.thumbnail("defaultThumbnail.jpg")
			.eventHall(eventHall)
			.build();
	}
}
