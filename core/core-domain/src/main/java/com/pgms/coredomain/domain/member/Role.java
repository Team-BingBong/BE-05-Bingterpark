package com.pgms.coredomain.domain.member;

import java.util.ArrayList;
import java.util.List;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
	private List<RolePermission> permissions = new ArrayList<>();

	public Role(String name) {
		this.name = name;
	}

	public void changeName(String name) {
		this.name = name;
	}
}
