package com.pgms.coredomain.domain.order;

public enum PaymentMethod {

    CARD("카드"),
    VIRTUAL_ACCOUNT("가상 계좌");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}
