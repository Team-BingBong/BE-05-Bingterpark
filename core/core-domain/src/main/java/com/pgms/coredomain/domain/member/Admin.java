package com.pgms.coredomain.domain.member;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.enums.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "admin")
public class Admin extends AccountBaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Column(name = "last_login_at", nullable = false)
	private LocalDateTime lastLoginAt;

	@ManyToOne
	private Group group;
}
