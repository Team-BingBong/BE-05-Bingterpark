package com.pgms.coredomain.domain.order;

import com.pgms.coredomain.domain.event.Ticket;
import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"order\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "order_name", nullable = false)
	private String orderName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status;

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

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<Ticket> tickets = new ArrayList<>();

	//TODO: 회원 매핑
}
