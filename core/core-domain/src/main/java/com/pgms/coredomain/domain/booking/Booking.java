package com.pgms.coredomain.domain.booking;

import java.util.ArrayList;
import java.util.List;

import com.pgms.coredomain.domain.common.BaseEntity;
import com.pgms.coredomain.domain.event.Ticket;
import com.pgms.coredomain.domain.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "booking_name", nullable = false)
	private String bookingName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private BookingStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "receipt_type", nullable = false)
	private ReceiptType receiptType;

	@Column(name = "buyer_name", nullable = false)
	private String buyerName;

	@Column(name = "buyer_phone_number", nullable = false)
	private String buyerPhoneNumber;

	@Column(name = "recipient_name")
	private String recipientName;

	@Column(name = "recipient_phone_number")
	private String recipientPhoneNumber;

	@Column(name = "street_address")
	private String streetAddress;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) //TODO: nullalbe = false 추가
	private Member member;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
	private List<Ticket> tickets = new ArrayList<>();

	@OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
	private Payment payment;

	@Builder
	public Booking(
		String id,
		String bookingName,
		BookingStatus status,
		ReceiptType receiptType,
		String buyerName,
		String buyerPhoneNumber,
		String recipientName,
		String recipientPhoneNumber,
		String streetAddress,
		String detailAddress,
		String zipCode,
		Integer amount,
		Member member
	) {
		this.id = id;
		this.bookingName = bookingName;
		this.status = status;
		this.receiptType = receiptType;
		this.buyerName = buyerName;
		this.buyerPhoneNumber = buyerPhoneNumber;
		this.recipientName = recipientName;
		this.recipientPhoneNumber = recipientPhoneNumber;
		this.streetAddress = streetAddress;
		this.detailAddress = detailAddress;
		this.zipCode = zipCode;
		this.amount = amount;
		this.member = member;
	}
}
