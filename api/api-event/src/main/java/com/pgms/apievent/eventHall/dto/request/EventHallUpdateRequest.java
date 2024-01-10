package com.pgms.apievent.eventHall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EventHallUpdateRequest(
	@NotBlank(message = "공연장 이름은 빈칸을 지정할 수 없습니다.")
	@Size(max = 10, message = "공연장 이름은 10자 이내로 입력해주세요.")
	String name,
	String address,
	List<EventHallSeatCreateRequest> eventHallSeatCreateRequests) {
}
