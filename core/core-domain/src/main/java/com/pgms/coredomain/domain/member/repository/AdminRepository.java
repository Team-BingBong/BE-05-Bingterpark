package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByEmail(String email);

	Slice<Admin> findSliceBy(Pageable pageable);

	boolean existsByRoleId(Long roleId);
}
