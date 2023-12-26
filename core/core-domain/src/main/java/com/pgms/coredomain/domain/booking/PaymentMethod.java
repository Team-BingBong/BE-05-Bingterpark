package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("카드"),
    VIRTUAL_ACCOUNT("가상 계좌");

    private final String description;
}
