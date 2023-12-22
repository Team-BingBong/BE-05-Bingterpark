package com.pgms.coredomain.domain.member;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.enums.AccountStatus;
import com.pgms.coredomain.domain.member.enums.Gender;
import com.pgms.coredomain.domain.member.enums.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member extends AccountBaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "birth_date", nullable = false)
	private String birthDate;

	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "street_address", nullable = false)
	private String streetAddress;

	@Column(name = "detail_address", nullable = false)
	private String detailAddress;

	@Column(name = "zipcode", nullable = false)
	private String zipcode;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Column(name = "provider", nullable = false)
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(name = "last_login_at", nullable = false)
	private LocalDateTime lastLoginAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Group group;
}
