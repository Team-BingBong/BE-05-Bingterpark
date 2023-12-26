package com.pgms.coredomain.domain.booking;

import com.pgms.coredomain.domain.event.Ticket;
import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

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

	@Column(name = "street_address")
	private String streetAddress;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "amount", nullable = false)
	private int amount;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
	private List<Ticket> tickets = new ArrayList<>();

	//TODO: 회원 매핑
}
