package com.pgms.coredomain.domain.order;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "payment_key", nullable = false)
    private String paymentKey;

    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "installment_plan_months")
    private int installmentPlanMonths;

    @Column(name = "is_interest_free")
    private boolean isInterestFree;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "depositor_name")
    private String depositorName;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "refund_ bank_code")
    private String refundBankCode;

    @Column(name = "refund_ account_number")
    private String refundAccountNumber;

    @Column(name = "refund_ holder_name")
    private String refundHolderName;

    @Column(name = "failed_msg")
    private String failedMsg;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "approved_at", nullable = false)
    private LocalDateTime approvedAt;

    // TODO: 예매 매칭
}
