package com.pgms.coredomain.domain.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("카드"),
    VIRTUAL_ACCOUNT("가상 계좌");

    private final String description;
}
