package com.pgms.coredomain.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.member.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByEmail(String email);

	Slice<Admin> findSliceBy(Pageable pageable);

	Optional<Admin> findByEmail(String email);

	@Query("select a from Admin a where a.lastLoginAt < :dateTime and a.status = 'ACTIVE'")
	List<Admin> findByLastLoginBeforeAndIsActive(@Param("dateTime") LocalDateTime dateTime);
}
