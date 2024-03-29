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

	@Enumerated(EnumType.STRING)
	@Column(name = "method")
	private PaymentMethod method;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private PaymentStatus status;

	@Column(name = "amount", nullable = false)
	private int amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "card_issuer")
	private CardIssuer cardIssuer;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "installment_plan_months")
	private int installmentPlanMonths;

	@Column(name = "is_interest_free")
	private boolean isInterestFree;

	@Column(name = "account_number")
	private String accountNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "bank_code")
	private BankCode bankCode;

	@Column(name = "depositor_name")
	private String depositorName;

	@Column(name = "due_date")
	private LocalDateTime dueDate;

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
	public Payment(PaymentMethod method, PaymentStatus status, int amount) {
		this.method = method;
		this.status = status;
		this.amount = amount;
	}

	public void updateBooking(Booking booking) {
		this.booking = booking;
	}

	public void updateCardInfo(CardIssuer cardIssuer, String cardNumber, int installmentPlanMonths, boolean isInterestFree) {
		this.cardIssuer = cardIssuer;
		this.cardNumber = cardNumber;
		this.installmentPlanMonths = installmentPlanMonths;
		this.isInterestFree = isInterestFree;
	}

	public void updateVirtualWaiting(String accountNumber, String bankCode, String depositorName,
		LocalDateTime dueDate) {
		this.accountNumber = accountNumber;
		this.bankCode = BankCode.getByBankNumCode(bankCode);
		this.depositorName = depositorName;
		this.dueDate = dueDate;
	}

	public void updateApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}

	public void updateConfirmInfo(String paymentKey, LocalDateTime requestedAt) {
		this.paymentKey = paymentKey;
		this.requestedAt = requestedAt;
	}

	public void updateStatus(PaymentStatus status) {
		this.status = status;
	}

	public void updateMethod(PaymentMethod method) {
		this.method = method;
	}

	public void updateFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}

	public boolean isCancelable() {
		return this.status == PaymentStatus.WAITING_FOR_DEPOSIT || this.status == PaymentStatus.DONE;
	}

	public boolean isCanceled() {
		return this.status == PaymentStatus.CANCELED;
	}

	public boolean isPaid() {
		return this.status == PaymentStatus.DONE;
	}
}
