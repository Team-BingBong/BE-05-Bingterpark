package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReceiptType {
	PICK_UP("현장수령"),
	DELIVERY("배송");

	private final String description;

	public static ReceiptType fromDescription(String description) {
		for (ReceiptType type : ReceiptType.values()) {
			if (type.description.equals(description)) {
				return type;
			}
		}
		throw new IllegalArgumentException("다음 수령 방법을 찾을 수 없습니다 : " + description);
	}
}
