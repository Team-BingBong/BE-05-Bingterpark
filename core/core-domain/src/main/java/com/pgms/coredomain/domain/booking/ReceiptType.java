package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReceiptType {
	PICK_UP("현장수령"),
	DELIVERY("배송");

	private final String description;
}
