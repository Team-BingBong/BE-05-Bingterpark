package com.pgms.coredomain.domain.order;

public enum PaymentStatus {

    DEPOSIT_PENDING("입금 대기"),
    CANCELED("취소"),
    COMPLETED("완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
