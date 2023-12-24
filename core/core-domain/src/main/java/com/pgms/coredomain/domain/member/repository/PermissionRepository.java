package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
