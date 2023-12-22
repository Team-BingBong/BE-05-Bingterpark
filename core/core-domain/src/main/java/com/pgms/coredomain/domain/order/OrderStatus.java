package com.pgms.coredomain.domain.order;

public enum OrderStatus {

    WAITING_FOR_DEPOSIT("입금대기"),
    PAYMENT_COMPLETED("결제완료"),
    CANCELLED("취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
