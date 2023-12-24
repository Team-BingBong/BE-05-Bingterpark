package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
