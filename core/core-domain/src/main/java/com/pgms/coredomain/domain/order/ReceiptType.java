package com.pgms.coredomain.domain.order;

public enum ReceiptType {
    PICK_UP("현장수령"),
    DELIVERY("배송");

    private final String description;

    ReceiptType(String description) {
        this.description = description;
    }
}
