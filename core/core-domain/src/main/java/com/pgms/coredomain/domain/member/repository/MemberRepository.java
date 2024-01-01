package com.pgms.coredomain.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pgms.coredomain.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByRoleId(Long roleId);

	@EntityGraph(attributePaths = {"role"})
	Slice<Member> findSliceBy(Pageable pageable);

	@Query("select m from Member m join fetch m.role r where m.id = :id")
	Optional<Member> findByIdWithRole(Long id);

	@Query("select m from Member m join fetch m.role r where m.id = :id and m.status = 'DELETED'")
	Optional<Member> findByIdAndIsDeletedTrue(Long id);

	@Query("select m from Member m join fetch m.role r where m.id = :id and m.status != 'DELETED'")
	Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
