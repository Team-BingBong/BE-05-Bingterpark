package com.pgms.coredomain.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.member.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	boolean existsByEmail(String email);

	boolean existsByRoleId(Long roleId);

	@EntityGraph(attributePaths = {"role"})
	Slice<Admin> findSliceBy(Pageable pageable);

	@Query("select a from Admin a join fetch a.role r where a.id = :id")
	Optional<Admin> findByIdWithRole(@Param("id") Long id);

	Optional<Admin> findByEmail(String email);

	@Query("select a from Admin a where a.lastLoginAt < :dateTime and a.status = 'ACTIVE'")
	List<Admin> findByLastLoginBeforeAndIsActive(@Param("dateTime") LocalDateTime dateTime);
}
