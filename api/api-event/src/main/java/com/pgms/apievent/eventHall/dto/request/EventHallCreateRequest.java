package com.pgms.apievent.eventHall.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EventHallCreateRequest(
	@NotBlank(message = "공연장 이름은 빈칸을 지정할 수 없습니다.")
	@Size(max = 25, message = "공연장 이름은 10자 이내로 입력해주세요.")
	String name,
	String address,
	List<EventHallSeatCreateRequest> eventHallSeatCreateRequests) {
}
