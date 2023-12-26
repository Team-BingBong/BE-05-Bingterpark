package com.pgms.coredomain.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByName(String name);

	Optional<Permission> findByNameAndIdNot(String name, Long id);
}
