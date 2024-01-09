package com.pgms.coredomain.domain.member;

import static com.pgms.coredomain.domain.member.enums.AccountStatus.*;

import com.pgms.coredomain.domain.member.enums.AccountStatus;
import com.pgms.coredomain.domain.member.enums.Gender;
import com.pgms.coredomain.domain.member.enums.Provider;
import com.pgms.coredomain.domain.member.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member extends AccountBaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "birth_date")
	private String birthDate;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "street_address")
	private String streetAddress;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Column(name = "provider")
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Builder
	public Member(
		String email,
		String password,
		String name,
		String phoneNumber,
		String birthDate,
		Gender gender,
		String streetAddress,
		String detailAddress,
		String zipCode,
		Provider provider
	) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.gender = gender;
		this.streetAddress = streetAddress;
		this.detailAddress = detailAddress;
		this.zipCode = zipCode;
		this.provider = provider;
		this.role = Role.ROLE_USER;
		this.status = ACTIVE;
	}

	public void updateMemberInfo(
		String name,
		String phoneNumber,
		String birthDate,
		Gender gender,
		String streetAddress,
		String detailAddress,
		String zipCode) {
		this.name = name != null ? name : this.name;
		this.phoneNumber = phoneNumber != null ? phoneNumber : this.phoneNumber;
		this.birthDate = birthDate != null ? birthDate : this.birthDate;
		this.gender = gender != null ? gender : this.gender;
		this.streetAddress = streetAddress != null ? streetAddress : this.streetAddress;
		this.detailAddress = detailAddress != null ? detailAddress : this.detailAddress;
		this.zipCode = zipCode != null ? zipCode : this.zipCode;
	}

	public boolean isDeleted() {
		return this.status == DELETED;
	}

	public boolean isLoginByProvider() {
		return this.provider != null;
	}

	public void updateToDeleted() {
		this.role = null;
		this.status = DELETED;
	}

	public void updateToActive() {
		this.role = Role.ROLE_USER;
		this.status = ACTIVE;
	}

	public void updatePassword(String encodedPassword) {
		this.password = encodedPassword;
		super.updateLastPasswordUpdatedAt();
	}
}
