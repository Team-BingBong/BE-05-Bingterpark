package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {

    READY("인증 전"),
    IN_PROGRESS("결제 수단, 소유자 인증 완료"),
    WAITING_FOR_DEPOSIT("가상계좌 입금대기"),
    DONE("결제 승인"),
    CANCELLED("결제 취소"),
    ABORTED("결제 승인 실패"),
    EXPIRED("유효 기간 만료");

    private final String description;
}
