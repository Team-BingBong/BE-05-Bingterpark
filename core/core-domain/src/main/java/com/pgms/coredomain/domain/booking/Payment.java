package com.pgms.coredomain.domain.booking;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "payment_key")
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

	@Column(name = "requested_at")
	private LocalDateTime requestedAt;

	@Column(name = "approved_at")
	private LocalDateTime approvedAt;

	@OneToOne
	@JoinColumn(name = "booking_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Booking booking;

	@Builder
	public Payment(PaymentMethod method, PaymentStatus status, int amount, Booking booking) {
		this.method = method;
		this.status = status;
		this.amount = amount;
		this.booking = booking;
	}

	public void updateCardInfo(String cardNumber, int installmentPlanMonths, boolean isInterestFree) {
		this.cardNumber = cardNumber;
		this.installmentPlanMonths = installmentPlanMonths;
		this.isInterestFree = isInterestFree;
	}

	public void updateConfirmInfo(String paymentKey, LocalDateTime approvedAt, LocalDateTime requestedAt) {
		this.paymentKey = paymentKey;
		this.approvedAt = approvedAt;
		this.requestedAt = requestedAt;
		this.status = PaymentStatus.DONE;
	}

	public void toAborted() {
		this.status = PaymentStatus.ABORTED;
	}

	public void toCanceled() {
		this.status = PaymentStatus.CANCELLED;
	}

	public void updateFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}
}
