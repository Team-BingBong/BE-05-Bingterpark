package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
}
