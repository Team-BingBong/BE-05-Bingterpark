package com.pgms.coredomain.domain.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("카드"),
    VIRTUAL_ACCOUNT("가상 계좌");

    private final String description;

    public static PaymentMethod fromDescription(String description) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.description.equals(description)) {
                return method;
            }
        }
        throw new IllegalArgumentException("다음 결제 수단을 찾을 수 없습니다 : " + description);
    }
}
