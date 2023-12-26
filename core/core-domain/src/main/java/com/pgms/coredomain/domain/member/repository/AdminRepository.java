package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
