package com.pgms.coredomain.domain.member;

import static lombok.AccessLevel.*;

import com.pgms.coredomain.domain.member.enums.AccountStatus;
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
@Table(name = "admin")
@NoArgsConstructor(access = PROTECTED)
public class Admin extends AccountBaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Builder
	public Admin(String name, String password, String phoneNumber, String email, Role role) {
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = AccountStatus.ACTIVE;
		this.role = role;
		super.updateLastLoginAt();
		super.updateLastPasswordUpdatedAt();
	}

	public void update(String name, String password, String phoneNumber, AccountStatus status, Role role) {
		this.name = name != null ? name : this.name;
		this.password = password != null ? password : this.password;
		this.phoneNumber = phoneNumber != null ? phoneNumber : this.phoneNumber;
		this.status = status != null ? status : this.status;
		this.role = role != null ? role : this.role;
		super.updateLastPasswordUpdatedAt();
	}

	public boolean isDeleted() {
		return this.status == AccountStatus.DELETED;
	}

	public boolean isNotActive() {
		return this.status != AccountStatus.ACTIVE;
	}

	public void updateToDeleted() {
		this.role = null;
		this.status = AccountStatus.DELETED;
	}

	public void updateToLocked() {
		this.status = AccountStatus.LOCKED;
	}

}
